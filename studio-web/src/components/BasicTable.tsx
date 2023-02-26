import { createStyles, Group, Pagination, PaginationProps, Paper, Table } from "@mantine/core";
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
    setPage: Dispatch<SetStateAction<number>>;
    total: number;
  } & PaginationProps;
}

const useStyles = createStyles({
  tableHeader: {
    textTransform: "uppercase",
  },
});

export default function BasicTable<T extends object>({ data, columns, sort, setSort, pagination }: BasicTableProps<T>) {
  const { classes } = useStyles();

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
    <Paper shadow="sm">
      <Table horizontalSpacing="md" verticalSpacing="sm" withBorder>
        <thead>
          {table.getHeaderGroups().map((headerGroup) => (
            <tr key={headerGroup.id}>
              {headerGroup.headers.map((header) => (
                <th key={header.id} onClick={header.column.getToggleSortingHandler()} className={classes.tableHeader}>
                  <Group spacing={6}>
                    {flexRender(header.column.columnDef.header, header.getContext())}
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
                <td key={cell.id}>{flexRender(cell.column.columnDef.cell, cell.getContext())}</td>
              ))}
            </tr>
          ))}
          {pagination && (
            <tr>
              <td colSpan={table.getAllLeafColumns().length}>
                <Pagination
                  position="right"
                  value={pagination.page + 1}
                  onChange={(e) => pagination.setPage(e - 1)}
                  size="sm"
                  {...pagination}
                />
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </Paper>
  );
}
