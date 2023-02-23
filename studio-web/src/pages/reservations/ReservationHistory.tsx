import { Badge, Box, Button, createStyles, Flex, Group, LoadingOverlay, Menu, Pagination, Paper, Table, Text } from "@mantine/core";
import { modals } from "@mantine/modals";
import { showNotification } from "@mantine/notifications";
import { IconArrowsSort, IconDotsVertical, IconSortAscending, IconSortDescending, IconX } from "@tabler/icons";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createColumnHelper, flexRender, getCoreRowModel, getSortedRowModel, SortingState, useReactTable } from "@tanstack/react-table";
import { AxiosError } from "axios";
import { useState, useMemo } from "react";
import { getErrorMessage } from "../../api/api";
import { cancelReservation, fetchReservationHistory } from "../../api/reservationService";
import {
  getReservationStatus,
  getReservationStatusColor,
  isStatusCancellable,
  ReservationStatusList,
  ReservationView,
} from "../../api/types";
import BasicTable from "../../components/BasicTable";
import { convertDatesToString } from "../../utils/DateTimeUtils";

const useStyles = createStyles({
  tableHeader: {
    textTransform: "uppercase",
  },
});

const queryKey = {
  reservationHistory: "reservationHistory",
};

export function ReservationHistory() {
  return (
    <Flex my="xl" direction="column" gap="xs">
      <Text size="xl" weight={600} mb="sm">
        Geçmiş Rezervasyonlar
      </Text>
      <HistoryTable />
    </Flex>
  );
}

function HistoryTable() {
  const { classes } = useStyles();
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<SortingState>([{ id: "startDate", desc: true }]);

  const historyQuery = useQuery({
    queryKey: [queryKey.reservationHistory, { page, sort }],
    queryFn: () => {
      return fetchReservationHistory(page, sort);
    },
    select: (data) => data.data,
    keepPreviousData: true,
  });

  const columnHelper = createColumnHelper<ReservationView>();
  const columns = useMemo(
    () => [
      columnHelper.accessor((row) => row.id, {
        id: "id",
        header: "#",
        size: 20,
        enableSorting: false,
      }),
      columnHelper.accessor((row) => row.slot, {
        id: "slotId",
        header: "Yer",
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
      columnHelper.accessor((row) => row, {
        id: "actions",
        header: "",
        enableSorting: false,
        cell: (row) => <ReservationActions reservation={row.getValue()} />,
      }),
    ],
    [],
  );

  switch (historyQuery.status) {
    case "loading":
      return (
        <Box pos="relative" h={600}>
          <LoadingOverlay transitionDuration={6000} visible />
        </Box>
      );
    case "error":
      return <Text>Error!</Text>;
  }

  if (historyQuery.data.numberOfElements === 0) {
    return <Text>No records found</Text>;
  }

  return (
    <>
      <BasicTable data={historyQuery.data?.content ?? []} columns={columns} sort={sort} setSort={setSort} />
      <Pagination position="right" value={page + 1} onChange={(index) => setPage(index - 1)} total={historyQuery.data?.totalPages ?? 1} />
    </>
  );
}

function ReservationActions({ reservation }: { reservation: ReservationView }) {
  return (
    <Flex justify="end">
      <Menu shadow="md" width={200} position="bottom-start" withArrow>
        <Menu.Target>
          <Button compact variant="default">
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
    onError: (error: AxiosError<{ message: string }>, _variables, _context) => {
      showNotification({
        id: "reservation-cancellation-error",
        title: "Rezervasyon İptali",
        message: getErrorMessage(error) ?? "Bilinmeyen bir hata oluştu!",
        color: "red",
        autoClose: 5000,
      });
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

  const cancellable = reservation.lastAction && isStatusCancellable(reservation.lastAction?.status);

  return (
    <Menu.Item color="red" icon={<IconX size={14} />} disabled={!cancellable} onClick={openModal}>
      İptal Et
    </Menu.Item>
  );
}
