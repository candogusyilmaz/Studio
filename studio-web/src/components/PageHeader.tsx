import { Text } from "@mantine/core";
import { useDocumentTitle } from "@mantine/hooks";

export default function PageHeader({ children }: { children: string }) {
  useDocumentTitle(children);

  return (
    <Text size={26} weight={500} mb="sm">
      {children}
    </Text>
  );
}
