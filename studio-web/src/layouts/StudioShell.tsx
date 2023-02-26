import { AppShell, Container } from "@mantine/core";
import { Outlet } from "react-router-dom";
import { HeaderMenu } from "../components/header/HeaderMenu";

export default function StudioShell() {
  return (
    <AppShell
      styles={(theme) => ({
        main: {
          background: theme.colorScheme === "dark" ? "#121212" : "#fafafb",
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
