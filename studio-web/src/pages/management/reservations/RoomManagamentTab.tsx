import { Button, Flex, Select, Modal, Pagination, Text, TextInput, SelectItem, NumberInput } from "@mantine/core";
import { useDebouncedState, useDisclosure } from "@mantine/hooks";
import { showNotification } from "@mantine/notifications";
import { IconHome, IconLocation, IconMapPin, IconPlus, IconUsers } from "@tabler/icons";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createColumnHelper, SortingState } from "@tanstack/react-table";
import { AxiosError } from "axios";
import { useEffect, useMemo, useState } from "react";
import { getErrorMessage } from "../../../api/api";
import { fetchLocationsByName } from "../../../api/locationService";
import { createRoom, fetchRooms } from "../../../api/roomService";
import { LocationView, RoomView } from "../../../api/types";
import BasicTable from "../../../components/BasicTable";

const queryKey = {
  roomManagementList: "roomManagementList",
  locationsQuery: "locationsQuery",
};

export default function RoomManagementTab() {
  return (
    <Flex direction="column" gap="xs">
      <Flex align="center" justify="space-between">
        <Text size="sm" mr="md">
          Oda Listesi
        </Text>
        <NewRoomButton />
      </Flex>
      <RoomTable />
    </Flex>
  );
}

function RoomTable() {
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<SortingState>([{ id: "name", desc: false }]);

  const roomQuery = useQuery({
    queryKey: [queryKey.roomManagementList, { page, sort }],
    queryFn: () => {
      return fetchRooms(page, sort);
    },
    select: (data) => data.data,
    keepPreviousData: true,
  });

  const columnHelper = createColumnHelper<RoomView>();
  const columns = useMemo(
    () => [
      columnHelper.accessor((row) => row.id, {
        id: "id",
        header: "#",
      }),
      columnHelper.accessor((row) => row.name, {
        id: "name",
        header: "Oda",
      }),
      columnHelper.accessor((row) => row.capacity, {
        id: "capacity",
        header: "Kapasite",
      }),
      columnHelper.accessor((row) => row.location?.name, {
        id: "location.name",
        header: "Lokasyon",
      }),
    ],
    [],
  );

  return (
    <>
      <BasicTable data={roomQuery.data?.content ?? []} columns={columns} sort={sort} setSort={setSort} />
      <Pagination position="right" value={page + 1} onChange={(index) => setPage(index - 1)} total={roomQuery.data?.totalPages ?? 1} />
    </>
  );
}

function NewRoomButton() {
  const queryClient = useQueryClient();
  const [opened, { open, close }] = useDisclosure(false);

  useEffect(() => {
    setName("");
    setCapacity("");
    setLocation(undefined);
  }, [opened]);

  const [name, setName] = useState("");
  const [capacity, setCapacity] = useState<number | "">("");
  const [location, setLocation] = useState<SelectItem & LocationView>();

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

  const newRoomMutation = useMutation({
    mutationFn: () => {
      let error = null;

      if (name.length < 3 || name.length > 27) {
        error = "Oda ismi 3 ila 27 karakter arasında olmalıdır.";
      } else if (!location || !location.id) {
        error = "Bağlı olduğu lokasyonu seçiniz.";
      }

      if (error) {
        showNotification({
          id: "room-create-error",
          title: "Oda Oluşturulurken Hata",
          message: error,
          color: "orange",
          autoClose: 5000,
        });
        return Promise.reject();
      }

      return createRoom(name, parseInt(capacity.toString()), location!.id);
    },
    onSuccess: () => {
      showNotification({
        id: "room-create-success",
        title: "Oda Oluşturuldu",
        message: "Yeni oda başarıyla oluşturuldu.",
        color: "green",
        autoClose: 5000,
      });
      queryClient.invalidateQueries({ queryKey: [queryKey.roomManagementList] });
      close();
    },
    onError: (error: AxiosError, _variables, _context) => {
      if (error instanceof AxiosError) {
        showNotification({
          id: "oda-create-error",
          title: "Oda Oluşturulurken Hata",
          message: getErrorMessage(error) ?? "Bilinmeyen bir hata oluştu!",
          color: "red",
          autoClose: 5000,
        });
      }
    },
  });

  return (
    <>
      <Modal.Root opened={opened} onClose={close} keepMounted={false}>
        <Modal.Overlay />
        <Modal.Content>
          <Modal.Header>
            <Modal.Title>Yeni Oda Oluşturma</Modal.Title>
            <Modal.CloseButton />
          </Modal.Header>
          <Modal.Body>
            <Flex direction="column" gap="sm">
              <TextInput
                icon={<IconHome size={16} />}
                label="Oda Adı"
                placeholder="Örn: Ortak Çalışma Odası 1"
                withAsterisk
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
              <NumberInput
                icon={<IconUsers size={16} />}
                label="Kapasite"
                placeholder="Oda kapasitesi"
                hideControls
                withAsterisk
                value={capacity}
                onChange={setCapacity}
                min={3}
                max={27}
              />
              <Select
                withinPortal
                withAsterisk
                clearable
                dropdownPosition="bottom"
                label="Lokasyon"
                data={locationQuery.data ?? []}
                placeholder="Bağlı olduğu lokasyonu seçiniz"
                searchable
                onSearchChange={setSearchQuery}
                nothingFound="Lokasyon bulunamadı"
                maxDropdownHeight={150}
                icon={<IconLocation size={16} />}
                value={location?.value ?? ""}
                onChange={(value) => setLocation(locationQuery.data?.find((s) => s.value === value))}
                disabled={locationQuery.isLoading}
              />
              <Flex justify="end" gap="md" mt="xs">
                <Button color="red" variant="subtle" onClick={close}>
                  İptal
                </Button>
                <Button onClick={() => newRoomMutation.mutate()} loading={newRoomMutation.isLoading} disabled={newRoomMutation.isSuccess}>
                  Oluştur
                </Button>
              </Flex>
            </Flex>
          </Modal.Body>
        </Modal.Content>
      </Modal.Root>

      <Button size="xs" variant="outline" onClick={open}>
        ODA OLUŞTUR
      </Button>
    </>
  );
}
