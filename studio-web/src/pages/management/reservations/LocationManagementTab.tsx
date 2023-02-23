import {
  Button,
  createStyles,
  Divider,
  Flex,
  Group,
  Select,
  Modal,
  Pagination,
  Paper,
  Table,
  Text,
  TextInput,
  SelectItem,
} from "@mantine/core";
import { useDebouncedState, useDisclosure } from "@mantine/hooks";
import { showNotification } from "@mantine/notifications";
import { IconArrowsSort, IconLocation, IconPlus, IconSortAscending, IconSortDescending } from "@tabler/icons";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createColumnHelper, flexRender, getCoreRowModel, getSortedRowModel, SortingState, useReactTable } from "@tanstack/react-table";
import { AxiosError, AxiosResponse } from "axios";
import { useMemo, useState } from "react";
import { getErrorMessage } from "../../../api/api";
import { createLocation, fetchLocations, fetchLocationsByName } from "../../../api/locationService";
import { LocationView, Page } from "../../../api/types";
import BasicTable from "../../../components/BasicTable";

const useStyles = createStyles({
  tableHeader: {
    textTransform: "uppercase",
  },
});

const queryKey = {
  locationManagementList: "locationManagementList",
  locationsQuery: "locationsQuery",
};

export default function LocationManagementTab() {
  return (
    <Flex direction="column" gap="xs">
      <Flex align="center" justify="space-between">
        <Text size="sm" mr="md">
          Lokasyon Listesi
        </Text>
        <NewLocationButton />
      </Flex>
      <LocationTable />
    </Flex>
  );
}

function LocationTable() {
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<SortingState>([{ id: "name", desc: false }]);

  const locationQuery = useQuery({
    queryKey: [queryKey.locationManagementList, { page, sort }],
    queryFn: () => {
      return fetchLocations(page, sort);
    },
    select: (data) => data.data,
    keepPreviousData: true,
  });

  const columnHelper = createColumnHelper<LocationView>();
  const columns = useMemo(
    () => [
      columnHelper.accessor((row) => row.id, {
        id: "id",
        header: "#",
      }),
      columnHelper.accessor((row) => row.name, {
        id: "name",
        header: "Lokasyon",
      }),
      columnHelper.accessor((row) => row.parent, {
        id: "parent.name",
        header: "Üst Lokasyon",
        cell: (row) => row.getValue()?.name,
      }),
    ],
    [],
  );

  return (
    <>
      <BasicTable data={locationQuery.data?.content ?? []} columns={columns} sort={sort} setSort={setSort} />
      <Pagination position="right" value={page + 1} onChange={(index) => setPage(index - 1)} total={locationQuery.data?.totalPages ?? 1} />
    </>
  );
}

function NewLocationButton() {
  const queryClient = useQueryClient();
  const [opened, { open, close }] = useDisclosure(false);

  const [name, setName] = useState("");
  const [parent, setParent] = useState<SelectItem & LocationView>();

  const [searchQuery, setSearchQuery] = useDebouncedState("", 1000);

  const locationQuery = useQuery({
    queryKey: [queryKey.locationsQuery, { searchQuery }],
    queryFn: () => fetchLocationsByName(`%${searchQuery}%`),
    select: (data) => data?.data?.content.map((s) => ({ ...s, value: s.id.toString(), label: s.name })),
    keepPreviousData: true,
    enabled: opened,
    cacheTime: 5 * 60 * 1000,
    staleTime: 5 * 60 * 1000,
  });

  const newLocationMutation = useMutation({
    mutationFn: () => {
      if (name.length < 3 || name.length > 27) {
        showNotification({
          id: "location-create-error",
          title: "Lokasyon Oluşturulurken Hata",
          message: "Lokasyon ismi 3 ila 27 karakter arasında olmalıdır",
          color: "orange",
          autoClose: 5000,
        });
        return Promise.reject();
      }

      return createLocation(name, parent?.id);
    },
    onSuccess: () => {
      showNotification({
        id: "location-create-success",
        title: "Lokasyon Oluşturuldu",
        message: "Yeni lokasyon başarıyla oluşturuldu.",
        color: "green",
        autoClose: 5000,
      });
      queryClient.invalidateQueries({ queryKey: [queryKey.locationManagementList] });
      close();
    },
    onError: (error: AxiosError, variables, context) => {
      showNotification({
        id: "location-create-error",
        title: "Lokasyon Oluşturulurken Hata",
        message: getErrorMessage(error) ?? "Bilinmeyen bir hata oluştu!",
        color: "red",
        autoClose: 5000,
      });
    },
  });

  return (
    <>
      <Modal.Root opened={opened} onClose={close}>
        <Modal.Overlay />
        <Modal.Content>
          <Modal.Header>
            <Modal.Title>Yeni Lokasyon Oluşturma</Modal.Title>
            <Modal.CloseButton />
          </Modal.Header>
          <Modal.Body>
            <Flex direction="column" gap="sm">
              <TextInput
                label="Lokasyon Adı"
                placeholder="Örn: Ankütek"
                withAsterisk
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
              <Select
                withinPortal
                clearable
                dropdownPosition="bottom"
                label="Üst Lokasyon"
                data={locationQuery.data ?? []}
                placeholder="Bir lokasyona bağlı ise seçiniz"
                searchable
                onSearchChange={setSearchQuery}
                nothingFound="Lokasyon bulunamadı"
                maxDropdownHeight={150}
                icon={<IconLocation size={16} />}
                value={parent?.value ?? ""}
                onChange={(value) => setParent(locationQuery.data?.find((s) => s.value === value))}
                disabled={locationQuery.isLoading}
              />
              <Flex justify="end" gap="md" mt="xs">
                <Button color="red" variant="subtle" onClick={close}>
                  İptal
                </Button>
                <Button onClick={() => newLocationMutation.mutate()}>Oluştur</Button>
              </Flex>
            </Flex>
          </Modal.Body>
        </Modal.Content>
      </Modal.Root>

      <Button size="xs" variant="default" leftIcon={<IconPlus size={16} />} onClick={open}>
        Yeni
      </Button>
    </>
  );
}
