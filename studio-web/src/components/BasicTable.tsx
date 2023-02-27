import { Box, createStyles, Divider, Flex, Group, Pagination, PaginationProps, Paper, Table, Text, useMantineTheme } from "@mantine/core";
import { IconArrowNarrowDown, IconArrowNarrowUp, IconSelector } from "@tabler/icons";
import { flexRender, getCoreRowModel, getSortedRowModel, SortDirection, SortingState, useReactTable } from "@tanstack/react-table";
import { Dispatch, SetStateAction } from "react";

interface BasicTableProps<T> {
  data: T[];
  columns: any[];
  sort?: SortingState;
  setSort?: Dispatch<SetStateAction<SortingState>>;
  pagination?: {
    page: number;
    onChange: Dispatch<SetStateAction<number>>;
    total: number;
  } & Omit<PaginationProps, "total" | "value" | "onChange">;
  tableHeader?: React.ReactNode;
}

const useStyles = createStyles((theme) => ({
  table: {
    "& th": {
      textTransform: "uppercase",
      padding: "56px",
    },
  },
}));

export default function BasicTable<T extends object>({ data, columns, sort, setSort, pagination, tableHeader }: BasicTableProps<T>) {
  const { classes } = useStyles();
  const theme = useMantineTheme();

  const table = useReactTable({
    data: data,
    state: { sorting: sort },
    columns: columns,
    manualSorting: true,
    onSortingChange: setSort,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
  });

  function getSortArrow(canSort: boolean, isSorted: false | SortDirection) {
    if (canSort && isSorted) {
      return (isSorted as string) === "asc" ? <IconArrowNarrowUp size={16} /> : <IconArrowNarrowDown size={16} />;
    } else if (canSort && !isSorted) {
      return <IconSelector size={16} />;
    }

    return null;
  }

  return (
    <Paper shadow="sm" withBorder radius="xs">
      {tableHeader && (
        <>
          <Box bg={theme.colorScheme === "dark" ? theme.colors.dark[6] : theme.colors.gray[1]} p="md">
            {tableHeader}
          </Box>
          <Divider />
        </>
      )}
      <Table horizontalSpacing="md" verticalSpacing="md" striped className={classes.table}>
        <thead>
          {table.getHeaderGroups().map((headerGroup) => (
            <tr key={headerGroup.id}>
              {headerGroup.headers.map((header) => (
                <th key={header.id} onClick={header.column.getToggleSortingHandler()}>
                  <Group spacing={6}>
                    <Text size="xs">{flexRender(header.column.columnDef.header, header.getContext())}</Text>
                    {sort && getSortArrow(header.column.getCanSort(), header.column.getIsSorted())}
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
                <td key={cell.id}>
                  <Text size="sm">{flexRender(cell.column.columnDef.cell, cell.getContext())}</Text>
                </td>
              ))}
            </tr>
          ))}
          {data.length === 0 && (
            <tr>
              <td colSpan={table.getAllLeafColumns().length}>
                <Flex align="center" justify="center">
                  <Text size="md" weight={300} italic>
                    Herhangi bir kayıt bulunamadı
                  </Text>
                </Flex>
              </td>
            </tr>
          )}
          {pagination && (
            <tr>
              <td colSpan={table.getAllLeafColumns().length}>
                <Pagination
                  {...pagination}
                  position="right"
                  value={pagination.page + 1}
                  size="sm"
                  onChange={(e) => pagination.onChange(e - 1)}
                />
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </Paper>
  );
}
