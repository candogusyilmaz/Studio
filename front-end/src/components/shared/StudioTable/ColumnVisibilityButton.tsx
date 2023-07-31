import { Button, Menu, Switch, Text } from "@mantine/core";
import { IconEye, IconEyeOff, IconLayoutColumns } from "@tabler/icons-react";
import { Table as TanstackTable } from "@tanstack/react-table";

export default function ColumnVisibilityButton<T>({ table }: { table: TanstackTable<T> }) {
  return (
    <Menu shadow="md" closeOnItemClick={false} styles={{ dropdown: { minWidth: 180 } }}>
      <Menu.Target>
        <Button size="xs" variant="default" px={6}>
          <IconLayoutColumns size={16} />
        </Button>
      </Menu.Target>

      <Menu.Dropdown>
        <Menu.Label>Sütün Görünürlüğü</Menu.Label>
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
