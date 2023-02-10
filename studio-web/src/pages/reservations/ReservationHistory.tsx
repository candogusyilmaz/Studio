import { Badge, Flex, Pagination, Paper, Table, Text } from "@mantine/core";
import { useQuery } from "@tanstack/react-query";
import { createColumnHelper, flexRender, getCoreRowModel, useReactTable } from "@tanstack/react-table";
import { useState, useMemo } from "react";
import { fetchReservationHistory } from "../../api/reservationService";
import { getReservationStatus, getReservationStatusColor, ReservationView } from "../../api/types";
import { convertDatesToString } from "../../utils/DateTimeUtils";

export function ReservationHistory() {
  const [page, setPage] = useState(0);

  const historyQuery = useQuery({
    queryKey: ["reservationHistory", page],
    queryFn: () => {
      return fetchReservationHistory(page);
    },
    select: (data) => data.data,
  });

  const columnHelper = createColumnHelper<ReservationView>();
  const columns = useMemo(
    () => [
      columnHelper.accessor((row) => row.id, {
        id: "#",
      }),
      columnHelper.accessor((row) => row.slot, {
        id: "Yer",
        cell: (row) => <Text>{`${row.getValue()?.room?.location?.name}, ${row.getValue()?.room?.name}, ${row.getValue()?.name}`}</Text>,
      }),
      columnHelper.accessor((row) => [row.startDate, row.endDate], {
        id: "Tarih",
        cell: (row) => <Text>{convertDatesToString(row.getValue()[0], row.getValue()[1])}</Text>,
      }),
      columnHelper.accessor((row) => row.lastAction?.status, {
        id: "Durum",
        cell: (row) => <Badge color={getReservationStatusColor(row.getValue())}>{getReservationStatus(row.getValue())}</Badge>,
      }),
    ],
    [],
  );

  const table = useReactTable({ data: historyQuery.data?.content ?? [], columns: columns, getCoreRowModel: getCoreRowModel() });

  return (
    <Flex my="xl" direction="column" gap="xs">
      <Text size="xl" weight={600} mb="sm">
        Geçmiş Rezervasyonlar
      </Text>
      <Paper>
        <Table horizontalSpacing="md" verticalSpacing="sm" withBorder>
          <thead>
            {table.getHeaderGroups().map((header) => (
              <tr key={header.id}>
                {header.headers.map((s) => (
                  <th key={s.id}>{s.isPlaceholder ? null : flexRender(s.column.columnDef.header, s.getContext())}</th>
                ))}
              </tr>
            ))}
          </thead>
          <tbody>
            {table.getRowModel().rows.map((row) => (
              <tr key={row.id} className='border-b" bg-white'>
                {row.getVisibleCells().map((cell) => (
                  <td key={cell.id}>{flexRender(cell.column.columnDef.cell, cell.getContext())}</td>
                ))}
              </tr>
            ))}
          </tbody>
        </Table>
      </Paper>
      <Pagination position="right" page={page + 1} onChange={(index) => setPage(index - 1)} total={historyQuery.data?.totalPages ?? 1} />
    </Flex>
  );
}
