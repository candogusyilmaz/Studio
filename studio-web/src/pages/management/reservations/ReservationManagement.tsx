import { Flex, Tabs, Text } from "@mantine/core";
import { IconHome, IconLocation } from "@tabler/icons";
import LocationManagementTab from "./LocationManagementTab";
import RoomManagementTab from "./RoomManagamentTab";

export default function ReservationManagement() {
  return (
    <Flex my="xl" direction="column" gap="xs">
      <Text size="xl" weight={600} mb="sm">
        Rezervasyon Tanımlamaları
      </Text>
      <Tabs orientation="horizontal" defaultValue="location" keepMounted={false}>
        <Tabs.List>
          <Tabs.Tab value="location" icon={<IconLocation size={14} />}>
            Lokasyon
          </Tabs.Tab>
          <Tabs.Tab value="room" icon={<IconHome size={14} />}>
            Oda
          </Tabs.Tab>
          <Tabs.Tab value="place">Yer</Tabs.Tab>
        </Tabs.List>

        <Tabs.Panel value="location" mt="xs">
          <LocationManagementTab />
        </Tabs.Panel>

        <Tabs.Panel value="room" mt="xs">
          <RoomManagementTab />
        </Tabs.Panel>

        <Tabs.Panel value="place" mt="xs">
          Settings tab content
        </Tabs.Panel>
      </Tabs>
    </Flex>
  );
}