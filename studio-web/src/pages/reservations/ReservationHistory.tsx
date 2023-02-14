import { Badge, Button, createStyles, Flex, Group, Pagination, Paper, Table, Text } from "@mantine/core";
import { IconArrowsSort, IconSortAscending, IconSortDescending } from "@tabler/icons";
import { useQuery } from "@tanstack/react-query";
import { createColumnHelper, flexRender, getCoreRowModel, getSortedRowModel, SortingState, useReactTable } from "@tanstack/react-table";
import { useState, useMemo } from "react";
import { fetchReservationHistory } from "../../api/reservationService";
import { getReservationStatus, getReservationStatusColor, ReservationView } from "../../api/types";
import { convertDatesToString } from "../../utils/DateTimeUtils";

const useStyles = createStyles({
  tableHeader: {
    textTransform: "uppercase",
  },
});

export function ReservationHistory() {
  const { classes } = useStyles();
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<SortingState>([{ id: "startDate", desc: true }]);

  const historyQuery = useQuery({
    queryKey: ["reservationHistory", page, sort],
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
      columnHelper.accessor((row) => row.id, {
        header: "Eylemler",
        enableSorting: false,
        cell: (row) => <Demo />,
      }),
    ],
    [],
  );

  const table = useReactTable({
    data: historyQuery.data?.content ?? [],
    state: { sorting: sort },
    columns: columns,
    onSortingChange: setSort,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
  });

  return (
    <Flex my="xl" direction="column" gap="xs">
      <Text size="xl" weight={600} mb="sm">
        Geçmiş Rezervasyonlar
      </Text>
      <Paper>
        <Table horizontalSpacing="md" verticalSpacing="sm" withBorder>
          <thead>
            {table.getHeaderGroups().map((headerGroup) => (
              <tr key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <th key={header.id} onClick={header.column.getToggleSortingHandler()} className={classes.tableHeader}>
                    <Group spacing={6}>
                      {flexRender(header.column.columnDef.header, header.getContext())}
                      {{
                        asc: <IconSortAscending size={16} />,
                        desc: <IconSortDescending size={16} />,
                      }[header.column.getIsSorted() as string] ?? null}
                      {header.column.getCanSort() && !header.column.getIsSorted() ? <IconArrowsSort size={16} /> : null}
                    </Group>
                  </th>
                ))}
              </tr>
            ))}
          </thead>
          <tbody>
            {table.getRowModel().rows.map((row) => (
              <tr key={row.id}>
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

function Demo() {
  return (
    <Button
      color="red"
      variant="subtle"
      compact
      uppercase
      styles={(theme) => ({
        root: {
          fontSize: "0.925em",
          color: "#d37373",
        },
      })}>
      İptal
    </Button>
  );
}
