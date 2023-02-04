import { Badge, Center, createStyles, Divider, Group, Paper, ScrollArea, Table, Text, TextInput, UnstyledButton } from "@mantine/core";
import { IconChevronUp, IconChevronDown, IconSelector, IconSearch } from "@tabler/icons";
import { useQuery } from "@tanstack/react-query";
import { createColumnHelper, flexRender, getCoreRowModel, useReactTable } from "@tanstack/react-table";
import { useState, useMemo } from "react";
import { fetchReservationHistory } from "../../api/reservationService";
import { getReservationStatus, getReservationStatusColor, ReservationView } from "../../api/types";
import { convertDatesToString } from "../../utils/DateTimeUtils";

export function History() {
  const historyQuery = useQuery({
    queryKey: ["reservationHistory"],
    queryFn: fetchReservationHistory,
    select: (data) => data.data,
    onSuccess: (data) => {
      console.log(data);
    },
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

  const table = useReactTable({ data: historyQuery.data ?? [], columns: columns, getCoreRowModel: getCoreRowModel() });

  return (
    <Paper withBorder p="xl" mih={600}>
      <Text size="xl" weight={600}>
        Geçmiş Rezervasyonlar
      </Text>
      <Divider mt="xs" mb="md" />
      <Table horizontalSpacing="md" verticalSpacing="xs" withBorder>
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
  );
}

const useStyles = createStyles((theme) => ({
  th: {
    padding: "0 !important",
  },

  control: {
    width: "100%",
    padding: `${theme.spacing.xs}px ${theme.spacing.md}px`,

    "&:hover": {
      backgroundColor: theme.colorScheme === "dark" ? theme.colors.dark[6] : theme.colors.gray[0],
    },
  },

  icon: {
    width: 21,
    height: 21,
    borderRadius: 21,
  },
}));

interface RowData {
  id: number;
  slot: string;
  startDate: Date;
  endDate: Date;
  lastAction: any;
}

interface TableSortProps {
  data: RowData[];
}

interface ThProps {
  children: React.ReactNode;
  reversed: boolean;
  sorted: boolean;
  onSort(): void;
}

function Th({ children, reversed, sorted, onSort }: ThProps) {
  const { classes } = useStyles();
  const Icon = sorted ? (reversed ? IconChevronUp : IconChevronDown) : IconSelector;
  return (
    <th className={classes.th}>
      <UnstyledButton onClick={onSort} className={classes.control}>
        <Group position="apart">
          <Text weight={500} size="sm">
            {children}
          </Text>
          <Center className={classes.icon}>
            <Icon size={14} stroke={1.5} />
          </Center>
        </Group>
      </UnstyledButton>
    </th>
  );
}

function filterData(data: RowData[], search: string) {
  const query = search.toLowerCase().trim();
  return data.filter((item) => item.slot.toLowerCase().includes(query));
}

function sortData(data: RowData[], payload: { sortBy: keyof RowData | null; reversed: boolean; search: string }) {
  const { sortBy } = payload;

  if (!sortBy) {
    return filterData(data, payload.search);
  }

  return filterData(
    [...data].sort((a, b) => {
      if (payload.reversed) {
        return b[sortBy].localeCompare(a[sortBy]);
      }

      return a[sortBy].localeCompare(b[sortBy]);
    }),
    payload.search,
  );
}

function TableSort({ data }: TableSortProps) {
  const [search, setSearch] = useState("");
  const [sortedData, setSortedData] = useState(data);
  const [sortBy, setSortBy] = useState<keyof RowData | null>(null);
  const [reverseSortDirection, setReverseSortDirection] = useState(false);

  const setSorting = (field: keyof RowData) => {
    const reversed = field === sortBy ? !reverseSortDirection : false;
    setReverseSortDirection(reversed);
    setSortBy(field);
    setSortedData(sortData(data, { sortBy: field, reversed, search }));
  };

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = event.currentTarget;
    setSearch(value);
    setSortedData(sortData(data, { sortBy, reversed: reverseSortDirection, search: value }));
  };

  const rows = sortedData.map((row) => (
    <tr key={row.id}>
      <td>{row.id}</td>
      <td>{row.slot}</td>
      <td>{row.startDate?.toLocaleDateString()}</td>
    </tr>
  ));

  return (
    <ScrollArea>
      <TextInput
        placeholder="Search by any field"
        mb="md"
        icon={<IconSearch size={14} stroke={1.5} />}
        value={search}
        onChange={handleSearchChange}
      />
      <Table horizontalSpacing="md" verticalSpacing="xs" sx={{ tableLayout: "fixed", minWidth: 700 }} withBorder>
        <thead>
          <tr>
            <Th sorted={sortBy === "id"} reversed={reverseSortDirection} onSort={() => setSorting("id")}>
              #
            </Th>
            <Th sorted={sortBy === "slot"} reversed={reverseSortDirection} onSort={() => setSorting("slot")}>
              Masa
            </Th>
            <Th sorted={sortBy === "startDate"} reversed={reverseSortDirection} onSort={() => setSorting("startDate")}>
              Tarih
            </Th>
          </tr>
        </thead>
        <tbody>
          {rows.length > 0 ? (
            rows
          ) : (
            <tr>
              <td colSpan={Object.keys(data[0]).length}>
                <Text weight={500} align="center">
                  Nothing found
                </Text>
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </ScrollArea>
  );
}
