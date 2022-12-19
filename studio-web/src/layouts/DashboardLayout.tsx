import { useState } from "react";
import { AppShell, Header, Footer, Text, MediaQuery, Burger, useMantineTheme } from "@mantine/core";
import { Sidebar } from "../components/sidebar/Sidebar";
import { Outlet } from "react-router-dom";

export default function DashboardLayout() {
  const theme = useMantineTheme();
  const [opened, setOpened] = useState(false);

  return (
    <AppShell
      layout="alt"
      styles={{
        main: {
          background: theme.colorScheme === "dark" ? theme.colors.dark[8] : theme.colors.gray[0],
        },
      }}
      navbarOffsetBreakpoint="sm"
      asideOffsetBreakpoint="sm"
      navbar={<Sidebar opened={opened} setOpened={() => setOpened((o) => !o)} />}
      fixed
      footer={
        <Footer height={60} p="md">
          Application footer
        </Footer>
      }
      header={
        <Header height={{ base: 67 }} p="md">
          <div style={{ display: "flex", alignItems: "center", height: "100%" }}>
            <MediaQuery largerThan="sm" styles={{ display: "none" }}>
              <Burger opened={opened} onClick={() => setOpened((o) => !o)} size="sm" color={theme.colors.gray[6]} mr="xl" />
            </MediaQuery>

            <Text>Application header</Text>
          </div>
        </Header>
      }>
      <Outlet />
    </AppShell>
  );
}
