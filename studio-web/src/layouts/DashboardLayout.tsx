import { useState } from "react";
import { AppShell, useMantineTheme, Container } from "@mantine/core";
import { Outlet } from "react-router-dom";
import { HeaderMenu } from "../components/header/HeaderMenu";

export default function DashboardLayout() {
  const theme = useMantineTheme();

  return (
    <AppShell
      styles={{
        main: {
          background: theme.colorScheme === "dark" ? theme.colors.dark[8] : "#f1f5f9",
        },
      }}
      //navbar={<Sidebar opened={opened} setOpened={() => setOpened((o) => !o)} />}
      /* footer={
        <Footer height={60} p="md">
          Application footer
        </Footer>
      } */
      header={<HeaderMenu />}
      layout="alt">
      <Container size="xl">
        <Outlet />
      </Container>
    </AppShell>
  );
}
