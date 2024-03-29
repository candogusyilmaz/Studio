import { Button, Flex, Modal, Select, Text, TextInput } from "@mantine/core";
import { useForm, zodResolver } from "@mantine/form";
import { useDisclosure } from "@mantine/hooks";
import { showNotification } from "@mantine/notifications";
import { IconLocation, IconPlus } from "@tabler/icons-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { SortingState, createColumnHelper } from "@tanstack/react-table";
import { useEffect, useMemo, useState } from "react";
import { z } from "zod";
import { showErrorNotification } from "../../../api/api";
import { createLocation, fetchLocations, fetchLocationsAll } from "../../../api/locationService";
import { LocationView } from "../../../api/types";
import StudioTable from "../../../components/shared/StudioTable/StudioTable";

const queryKey = {
  locationManagementList: "locationManagementList",
  allLocationsQuery: "allLocationsQuery",
};

export default function LocationManagementTab() {
  return <LocationTable />;
}

function LocationTable() {
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<SortingState>([{ id: "name", desc: false }]);

  const locationQuery = useQuery({
    queryKey: [queryKey.locationManagementList, { page, sort }],
    queryFn: ({ signal }) => {
      return fetchLocations(page, sort, signal);
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
      <StudioTable
        data={locationQuery.data?.content ?? []}
        columns={columns}
        sort={sort}
        setSort={setSort}
        pagination={{ page, setPage, total: locationQuery.data?.totalPages ?? 1 }}
        header={{
          leftSection: (
            <Text size="sm" mr="md">
              Lokasyon Listesi
            </Text>
          ),
          rightSection: <NewLocationButton />,
        }}
        status={locationQuery.status}
      />
    </>
  );
}

function NewLocationButton() {
  const queryClient = useQueryClient();
  const [opened, { open, close }] = useDisclosure(false);

  useEffect(() => {
    form.reset();
  }, [opened]);

  const locationQuery = useQuery({
    queryKey: [queryKey.allLocationsQuery],
    queryFn: ({ signal }) => fetchLocationsAll(signal),
    select: (data) =>
      data?.data?.map((s) => {
        let label = s.name;

        if (s.parent) {
          label = `${s.name}, ${s.parent.name}`;
        }

        return { ...s, value: s.id.toString(), label: label };
      }),
    keepPreviousData: true,
    enabled: opened,
    cacheTime: 5 * 60 * 1000,
    staleTime: 5 * 60 * 1000,
  });

  const newLocationMutation = useMutation({
    mutationFn: () => createLocation(form.values.name, form.values.parentId ? parseInt(form.values.parentId) : undefined),
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
    onError: (error, _variables, _context) => {
      showErrorNotification(error, { id: "location-create-error" });
    },
  });

  const schema = z.object({
    name: z.string().min(3, "3 karakterden kısa olamaz.").max(27, "27 karakterden uzun olamaz."),
  });

  const form = useForm({
    validate: zodResolver(schema),
    initialValues: {
      name: "",
      parentId: undefined,
    },
  });

  const handleSubmit = form.onSubmit((_values) => newLocationMutation.mutate());

  return (
    <>
      <Modal.Root opened={opened} onClose={close} keepMounted={false}>
        <Modal.Overlay />
        <Modal.Content>
          <Modal.Header>
            <Modal.Title>Yeni Lokasyon Oluşturma</Modal.Title>
            <Modal.CloseButton />
          </Modal.Header>
          <Modal.Body>
            <form onSubmit={handleSubmit}>
              <Flex direction="column" gap="sm">
                <TextInput label="Lokasyon Adı" placeholder="Örn: Ankütek" withAsterisk {...form.getInputProps("name")} />
                <Select
                  withinPortal
                  clearable
                  dropdownPosition="bottom"
                  label="Üst Lokasyon"
                  data={locationQuery.data ?? []}
                  placeholder="Bir lokasyona bağlı ise seçiniz"
                  searchable
                  nothingFound="Lokasyon bulunamadı"
                  maxDropdownHeight={150}
                  icon={<IconLocation size={16} />}
                  disabled={locationQuery.isLoading}
                  {...form.getInputProps("parentId")}
                />
                <Flex justify="end" gap="md" mt="xs">
                  <Button color="red" variant="subtle" onClick={close}>
                    İptal
                  </Button>
                  <Button type="submit" loading={newLocationMutation.isLoading}>
                    Oluştur
                  </Button>
                </Flex>
              </Flex>
            </form>
          </Modal.Body>
        </Modal.Content>
      </Modal.Root>

      <Button uppercase size="xs" variant="default" onClick={open} leftIcon={<IconPlus size={16} />}>
        Yeni
      </Button>
    </>
  );
}
