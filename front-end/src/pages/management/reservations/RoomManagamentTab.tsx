import { Button, Flex, Modal, NumberInput, Select, SelectItem, Text, TextInput } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { showNotification } from "@mantine/notifications";
import { IconHome, IconLocation, IconPlus, IconUsers } from "@tabler/icons-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { SortingState, createColumnHelper } from "@tanstack/react-table";
import { AxiosError } from "axios";
import { useEffect, useMemo, useState } from "react";
import { showErrorNotification } from "../../../api/api";
import { fetchLocationsAll } from "../../../api/locationService";
import { createRoom, fetchRooms } from "../../../api/roomService";
import { LocationView, RoomView } from "../../../api/types";
import StudioTable from "../../../components/shared/StudioTable/StudioTable";

const queryKey = {
  roomManagementList: "roomManagementList",
  locationsQueryAll: "locationsQueryAll",
};

export default function RoomManagementTab() {
  return <RoomTable />;
}

function RoomTable() {
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<SortingState>([{ id: "name", desc: false }]);

  const roomQuery = useQuery({
    queryKey: [queryKey.roomManagementList, { page, sort }],
    queryFn: ({ signal }) => fetchRooms(page, sort, signal),
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
      <StudioTable
        data={roomQuery.data?.content ?? []}
        columns={columns}
        sort={sort}
        setSort={setSort}
        pagination={{ page, setPage, total: roomQuery.data?.totalPages ?? 1 }}
        header={{
          leftSection: (
            <Text size="sm" mr="md">
              Oda Listesi
            </Text>
          ),
          rightSection: <NewRoomButton />,
          options: { showColumnVisibilityButton: true },
        }}
        status={roomQuery.status}
      />
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

  const locationQuery = useQuery({
    queryKey: [queryKey.locationsQueryAll],
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

  const newRoomMutation = useMutation({
    mutationFn: ({ name, capacity, locationId }: { name: string; capacity: number; locationId: number }) =>
      createRoom(name, capacity, locationId),
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
      showErrorNotification(error, { id: "room-create-error" });
    },
  });

  function createNewRoom() {
    let error = null;

    if (name.length < 3 || name.length > 27) {
      error = "Oda ismi 3 ila 27 karakter arasında olmalıdır.";
    } else if (!location || !location.id) {
      error = "Bağlı olduğu lokasyonu seçiniz.";
    }

    if (error) {
      return showErrorNotification(error, { id: "room-create-error" });
    }

    newRoomMutation.mutate({
      name,
      capacity: parseInt(capacity.toString()),
      locationId: location!.id,
    });
  }

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
                <Button onClick={createNewRoom} loading={newRoomMutation.isLoading}>
                  Oluştur
                </Button>
              </Flex>
            </Flex>
          </Modal.Body>
        </Modal.Content>
      </Modal.Root>

      <Button uppercase size="xs" variant="default" onClick={open} leftIcon={<IconPlus size={16} />}>
        Yeni
      </Button>
    </>
  );
}
