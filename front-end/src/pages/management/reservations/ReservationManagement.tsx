import { Flex, Tabs } from "@mantine/core";
import { IconHome, IconLocation } from "@tabler/icons-react";
import PageHeader from "../../../components/PageHeader";
import LocationManagementTab from "./LocationManagementTab";
import RoomManagementTab from "./RoomManagamentTab";

export default function ReservationManagement() {
  return (
    <Flex my="xl" direction="column" gap="xs">
      <PageHeader>Rezervasyon Tanımlamaları</PageHeader>
      <Tabs orientation="horizontal" defaultValue="location" keepMounted={false}>
        <Tabs.List>
          <Tabs.Tab value="location" icon={<IconLocation size={14} />}>
            Lokasyon
          </Tabs.Tab>
          <Tabs.Tab value="room" icon={<IconHome size={14} />}>
            Oda
          </Tabs.Tab>
        </Tabs.List>

        <Tabs.Panel value="location" mt="xs">
          <LocationManagementTab />
        </Tabs.Panel>

        <Tabs.Panel value="room" mt="xs">
          <RoomManagementTab />
        </Tabs.Panel>
      </Tabs>
    </Flex>
  );
}
