import {
  Box,
  Button,
  Center,
  createStyles,
  Divider,
  Flex,
  Group,
  LoadingOverlay,
  Menu,
  Overlay,
  Pagination,
  PaginationProps,
  Paper,
  Switch,
  Table,
  Text,
  useMantineTheme,
} from "@mantine/core";
import { IconArrowNarrowDown, IconArrowNarrowUp, IconEye, IconEyeOff, IconLayoutColumns, IconSelector } from "@tabler/icons";
import {
  flexRender,
  getCoreRowModel,
  getSortedRowModel,
  SortDirection,
  SortingState,
  Table as TanstackTable,
  useReactTable,
} from "@tanstack/react-table";
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
  } & Omit<PaginationProps, "total" | "value" | "onChange">;
  header?: {
    leftSection?: React.ReactNode;
    rightSection?: React.ReactNode;
    options?: {
      showDivider?: boolean;
      showSortButton?: boolean;
    };
  };
  status?: "loading" | "error" | "success";
}

const useStyles = createStyles((theme) => ({
  table: {
    "& th": {
      textTransform: "uppercase",
    },
  },
}));

export default function BasicTable<T extends object>({ data, columns, sort, setSort, pagination, header, status }: BasicTableProps<T>) {
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
      return (isSorted as string) === "asc" ? (
        <IconArrowNarrowUp style={{ paddingBottom: "0.05rem" }} size={14} stroke={3} />
      ) : (
        <IconArrowNarrowDown style={{ paddingBottom: "0.05rem" }} size={14} stroke={3} />
      );
    } else if (canSort && !isSorted) {
      return <IconSelector style={{ paddingBottom: "0.05rem" }} size={14} stroke={3} />;
    }

    return null;
  }

  if (status && status !== "success") return <TableOverlay status={status} />;

  return (
    <Paper shadow="sm" withBorder radius="xs">
      {header && (
        <>
          <Flex
            bg={theme.colorScheme === "dark" ? theme.colors.dark[6] : theme.colors.gray[1]}
            p="xs"
            align="center"
            justify="space-between">
            <Group>{header.leftSection}</Group>
            <Group spacing={12}>
              {header.rightSection}
              {header.options?.showDivider && <Divider orientation="vertical" size="xs" />}
              {header.options?.showSortButton && <SortButton table={table} />}
            </Group>
          </Flex>
          <Divider />
        </>
      )}
      <Table horizontalSpacing="md" verticalSpacing="md" striped className={classes.table}>
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
                    {sort && getSortArrow(header.column.getCanSort(), header.column.getIsSorted())}
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
                    Herhangi bir kay??t bulunamad??
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
                  onChange={(e) => pagination.setPage(e - 1)}
                />
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </Paper>
  );
}

function TableOverlay({ status }: { status: "loading" | "error" }) {
  const h = "min(60vh, 600px)";

  switch (status) {
    case "loading":
      return (
        <Box pos="relative" h={h}>
          <LoadingOverlay loaderProps={{ variant: "bars" }} transitionDuration={6000} visible />
        </Box>
      );
    case "error":
      return (
        <Box pos="relative" h={h}>
          <Overlay
            styles={(theme) => ({
              root: {
                backgroundColor: theme.colorScheme === "dark" ? theme.colors.dark[6] : theme.colors.gray[0],
                border: "1px solid rgba(255,0,0,0.4)",
              },
            })}>
            <Center h={h}>
              <Text>Bir hata olu??tu!</Text>
            </Center>
          </Overlay>
        </Box>
      );
    default:
      return null;
  }
}

function SortButton<T>({ table }: { table: TanstackTable<T> }) {
  return (
    <Menu shadow="md" closeOnItemClick={false} styles={{ dropdown: { minWidth: 180 } }}>
      <Menu.Target>
        <Button size="xs" variant="default" px={6}>
          <IconLayoutColumns size={16} />
        </Button>
      </Menu.Target>

      <Menu.Dropdown>
        <Menu.Label>S??t??n G??r??n??rl??????</Menu.Label>
        {table.getAllLeafColumns().map((column) => {
          if (!column.columnDef.header) return null;

          return (
            <Menu.Item
              key={column.id}
              py={4}
              rightSection={
                <Switch
                  checked={column.getIsVisible()}
                  onChange={column.getToggleVisibilityHandler()}
                  onLabel={<IconEye size={14} />}
                  offLabel={<IconEyeOff size={14} />}
                  size="xs"
                />
              }>
              <Text size="xs" weight={500}>
                {column.columnDef.header?.toString()}
              </Text>
            </Menu.Item>
          );
        })}
      </Menu.Dropdown>
    </Menu>
  );
}
