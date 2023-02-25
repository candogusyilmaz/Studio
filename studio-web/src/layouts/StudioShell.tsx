import { AppShell, Container } from "@mantine/core";
import { Outlet } from "react-router-dom";
import { HeaderMenu } from "../components/header/HeaderMenu";

export default function StudioShell() {
  return (
    <AppShell
      styles={(theme) => ({
        main: {
          background: theme.colorScheme === "dark" ? theme.colors.dark[8] : "#f1f5f9",
          minHeight: "calc(100vh - 112px)",
          [theme.fn.smallerThan("sm")]: {
            minHeight: "calc(100vh - 56px)",
          },
        },
      })}
      header={<HeaderMenu />}
      layout="alt">
      <Container size="xl">
        <Outlet />
      </Container>
    </AppShell>
  );
}
