import { Box, Divider, Flex, Group, Pagination, Paper, Table, Text, createStyles, useMantineTheme } from "@mantine/core";
import { flexRender, getCoreRowModel, getSortedRowModel, useReactTable } from "@tanstack/react-table";
import SortArrow from "./SortArrow";
import ColumnVisibilityButton from "./SortButton";
import TableOverlay from "./TableOverlay";
import { StudioTableProps } from "./types";

const useStyles = createStyles((theme) => ({
  table: {
    minWidth: "100%",

    "& th": {
      overflow: "hidden",
      textTransform: "uppercase",
    },

    "& td": {
      overflow: "hidden",
    },

    "& tr": {
      width: "fit-content",
    },
  },
}));

export default function StudioTable<T extends object>({ data, columns, sort, setSort, pagination, header, status }: StudioTableProps<T>) {
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

  if (status && status !== "success") return <TableOverlay status={status} />;

  return (
    <Paper shadow="sm" withBorder radius="xs">
      <Box m={15}>
        {header && (
          <Flex
            bg={theme.colorScheme === "dark" ? theme.colors.dark[6] : theme.colors.gray[1]}
            p="xs"
            align="center"
            justify="space-between">
            <Group>{header.leftSection}</Group>
            <Group spacing={12}>
              {header.rightSection}
              {header.options?.showDivider && <Divider orientation="vertical" size="xs" />}
              {header.options?.showColumnVisibilityButton && <ColumnVisibilityButton table={table} />}
            </Group>
          </Flex>
        )}
        <Table withBorder withColumnBorders horizontalSpacing="md" verticalSpacing="xs" striped className={classes.table}>
          <thead>
            {table.getHeaderGroups().map((headerGroup) => (
              <tr key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <th key={header.id} onClick={header.column.getToggleSortingHandler()}>
                    <Flex
                      gap={3}
                      align="center"
                      style={{
                        cursor: header.column.getCanSort() ? "pointer" : "auto",
                      }}>
                      <Text size="xs" style={{ lineHeight: 1 }}>
                        {flexRender(header.column.columnDef.header, header.getContext())}
                      </Text>
                      {sort && <SortArrow canSort={header.column.getCanSort()} isSorted={header.column.getIsSorted()} />}
                    </Flex>
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
          </tbody>
        </Table>
        {pagination && (
          <Pagination
            mt="xs"
            position="right"
            value={pagination.page + 1}
            size="sm"
            onChange={(e) => e && pagination.setPage(e - 1)}
            {...(pagination.props, { total: pagination.total })}
          />
        )}
      </Box>
    </Paper>
  );
}
