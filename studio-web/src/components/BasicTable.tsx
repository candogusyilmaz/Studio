import { createStyles, Group, Paper, Table } from "@mantine/core";
import { IconArrowsSort, IconSortAscending, IconSortDescending } from "@tabler/icons";
import { flexRender, getCoreRowModel, getSortedRowModel, SortingState, useReactTable } from "@tanstack/react-table";
import { Dispatch, SetStateAction } from "react";

interface BasicTableProps<T> {
  data: T[];
  columns: any[];
  sort: SortingState;
  setSort: Dispatch<SetStateAction<SortingState>>;
}

const useStyles = createStyles({
  tableHeader: {
    textTransform: "uppercase",
  },
});

export default function BasicTable<T extends object>({ data, columns, sort, setSort }: BasicTableProps<T>) {
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

  return (
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
  );
}
