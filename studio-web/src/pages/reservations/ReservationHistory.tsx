import { Badge, Button, Flex, Menu, Text } from "@mantine/core";
import { modals } from "@mantine/modals";
import { IconDotsVertical, IconX } from "@tabler/icons-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { SortingState, createColumnHelper } from "@tanstack/react-table";
import { useState } from "react";
import { showErrorNotification } from "../../api/api";
import { cancelReservation, fetchReservationHistory } from "../../api/reservationService";
import { ReservationView, getReservationStatus, getReservationStatusColor, isReservationCancellable } from "../../api/types";
import StudioTable from "../../components/shared/StudioTable/StudioTable";
import Page1 from "../../layouts/Page1";
import { convertDateToLocaleDateTimeString, convertDatesToString } from "../../utils/DateTimeUtils";

const queryKey = {
  reservationHistory: "reservationHistory",
};

export default function ReservationHistory() {
  return (
    <Page1 header="Geçmiş Rezervasyonlarım">
      <HistoryTable />
    </Page1>
  );
}

const columnHelper = createColumnHelper<ReservationView>();
const columns = [
  columnHelper.accessor((row) => row.id, {
    id: "id",
    header: "#",
    size: 20,
    enableSorting: false,
  }),
  columnHelper.accessor((row) => row.slot, {
    id: "slotId",
    header: "Lokasyon",
    enableSorting: false,
    cell: (row) => <Text>{`${row.getValue()?.room?.location?.name}, ${row.getValue()?.room?.name}, ${row.getValue()?.name}`}</Text>,
  }),
  columnHelper.accessor((row) => [row.startDate, row.endDate], {
    id: "startDate",
    header: "Tarih",
    cell: (row) => <Text>{convertDatesToString(row.getValue()[0], row.getValue()[1])}</Text>,
  }),
  columnHelper.accessor((row) => row.lastAction?.status, {
    id: "lastAction.status",
    header: "Durum",
    cell: (row) => <Badge color={getReservationStatusColor(row.getValue())}>{getReservationStatus(row.getValue())}</Badge>,
  }),
  columnHelper.accessor((row) => row.lastAction?.actionDate, {
    id: "lastAction.actionDate",
    header: "Son Güncelleme",
    cell: (row) => convertDateToLocaleDateTimeString(row.getValue()!),
  }),
  columnHelper.accessor((row) => row, {
    id: "actions",
    header: "",
    enableSorting: false,
    cell: (row) => <ReservationActions reservation={row.getValue()} />,
  }),
];

function HistoryTable() {
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<SortingState>([{ id: "lastAction.actionDate", desc: true }]);

  const historyQuery = useQuery({
    queryKey: [queryKey.reservationHistory, { page, sort }],
    queryFn: ({ signal }) => fetchReservationHistory(page, sort, signal),
    select: (data) => data.data,
    keepPreviousData: true,
    staleTime: 10 * 60 * 1000,
  });

  return (
    <StudioTable
      data={historyQuery.data?.content ?? []}
      columns={columns}
      sort={sort}
      setSort={setSort}
      pagination={{ page, setPage, total: historyQuery.data?.totalPages ?? 1 }}
      header={{ options: { showColumnVisibilityButton: true } }}
      status={historyQuery.status}
    />
  );
}

function ReservationActions({ reservation }: { reservation: ReservationView }) {
  return (
    <Flex justify="end">
      <Menu shadow="md" width={200} position="bottom" withinPortal>
        <Menu.Target>
          <Button
            compact
            variant="subtle"
            styles={(theme) => ({
              root: {
                color: theme.colorScheme === "dark" ? theme.colors.dark[0] : theme.colors.gray[7],
              },
            })}>
            <IconDotsVertical size={14} />
          </Button>
        </Menu.Target>

        <Menu.Dropdown>
          <ReservationCancellationMenuItem reservation={reservation} />
        </Menu.Dropdown>
      </Menu>
    </Flex>
  );
}

function ReservationCancellationMenuItem({ reservation }: { reservation: ReservationView }) {
  const queryClient = useQueryClient();
  const reservationCancelMutation = useMutation({
    mutationFn: () => {
      return cancelReservation(reservation.id);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.reservationHistory] });
    },
    onError: (error, _variables, _context) => {
      showErrorNotification(error, { id: "reservation-cancellation-error" });
    },
  });

  const openModal = () =>
    modals.openConfirmModal({
      modalId: "reservation-cancellation-modal",
      title: "Rezervasyon İptali",
      children: (
        <Text size="sm">
          {convertDatesToString(reservation.startDate, reservation.endDate)} tarihli rezervasyonunuzu iptal etmek istediğinizden emin
          misiniz?
        </Text>
      ),
      labels: { confirm: "Evet, iptal et", cancel: "Hayır" },
      confirmProps: { color: "red" },
      onConfirm: () => reservationCancelMutation.mutate(),
    });

  const cancellable = reservation.lastAction && isReservationCancellable(reservation.lastAction?.status);

  return (
    <Menu.Item color="red" icon={<IconX size={14} />} disabled={!cancellable} onClick={openModal}>
      İptal Et
    </Menu.Item>
  );
}
