import { AppShell, useMantineTheme, Container } from "@mantine/core";
import { Outlet } from "react-router-dom";
import { HeaderMenu } from "../components/header/HeaderMenu";

export default function StudioShell() {
  const theme = useMantineTheme();

  return (
    <AppShell
      styles={{
        main: {
          background: theme.colorScheme === "dark" ? theme.colors.dark[8] : "#f1f5f9",
        },
      }}
      header={<HeaderMenu />}
      layout="alt">
      <Container size="xl">
        <Outlet />
      </Container>
    </AppShell>
  );
}
