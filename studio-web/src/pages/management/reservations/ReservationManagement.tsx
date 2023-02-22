import { Flex, Tabs, Text } from "@mantine/core";
import LocationManagementTab from "./LocationManagementTab";

export default function ReservationManagement() {
  return (
    <Flex my="xl" direction="column" gap="xs">
      <Text size="xl" weight={600} mb="sm">
        Rezervasyon Tanımlamaları
      </Text>
      <Tabs orientation="vertical" variant="outline" defaultValue="location">
        <Tabs.List>
          <Tabs.Tab value="location">Lokasyon</Tabs.Tab>
          <Tabs.Tab value="room">Oda</Tabs.Tab>
          <Tabs.Tab value="place">Yer</Tabs.Tab>
        </Tabs.List>

        <Tabs.Panel value="location" pl="xl">
          <LocationManagementTab />
        </Tabs.Panel>

        <Tabs.Panel value="room" pl="xl">
          Messages tab content
        </Tabs.Panel>

        <Tabs.Panel value="place" pl="xl">
          Settings tab content
        </Tabs.Panel>
      </Tabs>
    </Flex>
  );
}
