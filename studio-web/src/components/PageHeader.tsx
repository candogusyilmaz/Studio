import { Text } from "@mantine/core";

export default function PageHeader({ children }: { children: string }) {
  return (
    <Text size={26} weight={500} mb="sm">
      {children}
    </Text>
  );
}
