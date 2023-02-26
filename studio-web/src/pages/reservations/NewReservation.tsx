import {
  Button,
  Container,
  createStyles,
  Flex,
  Group,
  Paper,
  Radio,
  RangeSlider,
  ScrollArea,
  Select,
  SelectItem,
  SimpleGrid,
  Space,
  Table,
  Text,
} from "@mantine/core";
import { DatePickerInput } from "@mantine/dates";
import { showNotification } from "@mantine/notifications";
import { IconCalendarEvent, IconHome, IconLocation } from "@tabler/icons";
import { useMutation, useQuery } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { getErrorMessage } from "../../api/api";
import { createReservation } from "../../api/reservationService";
import { fetchAvailableSlots } from "../../api/slotService";
import { ItemView, LocationView } from "../../api/types";
import { convertNumberToDate, convertNumberToShortTimeString } from "../../utils/DateTimeUtils";

const useStyles = createStyles((theme) => ({
  boxedTextTitle: {
    lineHeight: "1.5",
    display: "inline-block",
  },

  boxedText: {
    padding: "12px 24px",
    border: `1px solid ${theme.colorScheme === "dark" ? theme.colors.dark[5] : theme.colors.gray[4]}`,
    textAlign: "center",
    color: theme.colorScheme === "dark" ? theme.colors.gray[6] : theme.colors.dark[2],
  },

  sliderLabel: {
    marginTop: "53px",
  },
}));

const queryKey = {
  availableSlots: "availableSlots",
};

function useSlotsQuery(date: Date | null, timeRange: [number, number]) {
  const [locations, setLocations] = useState<(SelectItem & LocationView)[]>([]);

  const slotsQuery = useQuery({
    queryKey: [queryKey.availableSlots, date, timeRange],
    queryFn: ({ signal }) => {
      const startDate = convertNumberToDate(timeRange[0], new Date(date!), 59); // can't be null because query is enabled if date is not null
      const endDate = convertNumberToDate(timeRange[1], new Date(date!), 1);

      return fetchAvailableSlots(startDate, endDate, undefined, signal);
    },
    enabled: date !== null,
    select: (data) => data?.data,
    onSuccess: (data) => {
      const uniqueLocations: typeof locations = [];
      data?.forEach((slot) => {
        if (uniqueLocations.some((x) => x.value === slot.room!.location!.id.toString())) return;

        uniqueLocations.push({
          ...slot,
          value: slot.room!.location!.id.toString(),
          label: slot.room!.location!.name,
        });
      });
      setLocations(uniqueLocations);
    },
  });

  return { slotsQuery, locations, setLocations };
}

export function NewReservation() {
  const { classes } = useStyles();

  const navigate = useNavigate();

  const [date, setDate] = useState<Date | null>(null);
  const [timeRange, setTimeRange] = useState<[number, number]>([17, 35]);
  const [timeRangeEnd, setTimeRangeEnd] = useState<[number, number]>([17, 35]);

  const [rooms, setRooms] = useState<SelectItem[]>([]);
  const [slots, setSlots] = useState<SelectItem[]>([]);

  const [selectedLocationId, setSelectedLocationId] = useState<string | null>(null);
  const [selectedRoomId, setSelectedRoomId] = useState<string | null>(null);
  const [selectedSlotId, setSelectedSlotId] = useState<string | null>(null);

  const { slotsQuery, locations, setLocations } = useSlotsQuery(date, timeRangeEnd);

  function handleDateChange(dateVal: Date) {
    setDate(dateVal);
    clearForm();
  }

  function handleTimeRangeChangeEnd(value: [number, number]) {
    setTimeRangeEnd(value);
    clearForm();
  }

  function clearForm() {
    setSelectedLocationId(null);
    setSelectedRoomId(null);
    setSelectedSlotId(null);

    setLocations([]);
    setRooms([]);
    setSlots([]);
  }

  function clearFormAll() {
    setSelectedLocationId(null);
    setSelectedRoomId(null);
    setSelectedSlotId(null);
    setDate(null);
    setTimeRange([17, 35]);
    setTimeRangeEnd([17, 35]);

    setLocations([]);
    setRooms([]);
    setSlots([]);
  }

  function handleLocationChange(locationId: string) {
    setSelectedLocationId(locationId);

    // populate the slots
    const tempRooms: SelectItem[] = [];
    slotsQuery.data?.forEach((s) => {
      if (!s.room || !s.room.location) {
        return;
      }

      if (tempRooms.some((x) => x.value === s.room!.id.toString() || s.room!.location!.id.toString() !== locationId)) return;
      tempRooms.push({ value: s.room.id.toString(), label: s.room.name });
    });

    setRooms(tempRooms);
    setSelectedRoomId(null);
    setSelectedSlotId(null);
    setSlots([]);
  }

  function handleRoomChange(roomId: string) {
    setSelectedRoomId(roomId);

    const tempSlots: SelectItem[] = [];
    slotsQuery.data?.forEach((s) => {
      if (!s.room) return;

      if (s.room.id === parseInt(roomId)) {
        tempSlots.push({ value: s.id.toString(), label: s.name });
      }
    });

    setSlots(tempSlots);
    setSelectedSlotId(null);
  }

  const reservationMutation = useMutation({
    mutationFn: () => {
      if (!selectedSlotId || !date || !timeRangeEnd[0] || !timeRangeEnd[1]) {
        return Promise.reject();
      }

      const startDate = convertNumberToDate(timeRangeEnd[0], new Date(date), 59);
      const endDate = convertNumberToDate(timeRangeEnd[1], new Date(date), 1);

      /* if (dayjs(startDate).isBefore(new Date())) {
        showNotification({
          id: "reservation-create-error",
          title: "Rezervasyon",
          message: "Geçmişe dair rezervasyon yapamazsınız.",
          color: "red",
          autoClose: 5000,
        });
        return Promise.reject();
      } */

      return createReservation(parseInt(selectedSlotId), startDate, endDate);
    },
    onSuccess: () => {
      showNotification({
        id: "reservation-create-success",
        title: "Rezervasyon",
        message: "Rezervasyonunuz başarıyla oluşturuldu.",
        color: "green",
        autoClose: 5000,
      });
      clearFormAll();
      navigate("/reservations/history");
    },
    onError: (error: AxiosError, _variables, _context) => {
      showNotification({
        id: "reservation-create-error",
        title: "Rezervasyon",
        message: getErrorMessage(error) ?? "Bilinmeyen bir hata oluştu!",
        color: "red",
        autoClose: 5000,
      });
    },
  });

  return (
    <Container size="xs" mt="xl">
      <Paper withBorder p="xl">
        <Flex direction="column" gap="sm">
          <Text size="xl" weight={600}>
            Rezervasyon Bilgileri
          </Text>
          <DatePickerInput
            placeholder="Rezervasyon yapmak istediğiniz gün"
            label="Tarih"
            icon={<IconCalendarEvent size={16} />}
            minDate={new Date()}
            value={date}
            onChange={handleDateChange}
          />
          <div>
            <Text size="sm" weight={500} className={classes.boxedTextTitle}>
              Zaman
            </Text>
            <div>
              <RangeSlider
                min={0}
                max={48}
                labelAlwaysOn
                label={convertNumberToShortTimeString}
                value={timeRange}
                onChange={setTimeRange}
                onChangeEnd={handleTimeRangeChangeEnd}
                marks={[{ value: 17 }, { value: 24 }, { value: 25 }, { value: 27 }, { value: 35 }]}
                classNames={{ label: classes["sliderLabel"] }}
                minRange={4}
              />
            </div>
          </div>
          <Space mt="md" />
          <Select
            label="Lokasyon"
            placeholder={date ? "Lokasyon seçiniz" : "Lokasyonlar için tarih seçiniz"}
            searchable
            nothingFound="Lokasyon bulunamadı"
            data={locations}
            maxDropdownHeight={280}
            icon={<IconLocation size={16} />}
            value={selectedLocationId}
            onChange={handleLocationChange}
            disabled={!date || slotsQuery.isLoading}
          />
          <Select
            label="Oda"
            placeholder={selectedLocationId ? "Oda seçiniz" : "Odalar için lokasyon seçiniz"}
            searchable
            nothingFound="Oda bulunamadı"
            data={rooms}
            maxDropdownHeight={280}
            icon={<IconHome size={16} />}
            disabled={selectedLocationId === null}
            value={selectedRoomId}
            onChange={handleRoomChange}
          />
          {slots.length > 0 ? (
            <Radio.Group name="whereToSit" label="Nerede oturmak istiyorsunuz?" value={selectedSlotId ?? "-1"} onChange={setSelectedSlotId}>
              <ScrollArea mah={320} type="auto">
                <SimpleGrid
                  mah={300}
                  cols={4}
                  mt="xs"
                  breakpoints={[
                    {
                      maxWidth: "xs",
                      cols: 2,
                    },
                    {
                      maxWidth: "sm",
                      cols: 3,
                    },
                  ]}>
                  {slots?.map((slot) => (
                    <Radio value={slot.value} label={slot.label} key={slot.value} disabled={!selectedRoomId} />
                  ))}
                </SimpleGrid>
              </ScrollArea>
            </Radio.Group>
          ) : (
            <div>
              <Text size="sm" weight={500} className={classes.boxedTextTitle}>
                Nerede oturmak istiyorsunuz?
              </Text>
              <Text className={classes.boxedText} weight={400} size="sm">
                Masaları listelemek için oda seçiniz
              </Text>
            </div>
          )}
          <div>
            <Text size="sm" weight={500} className={classes.boxedTextTitle}>
              Masa Envanteri
            </Text>
            <ItemsTable items={slotsQuery.data?.find((s) => s.id.toString() === selectedSlotId)?.items} />
          </div>
          <Group position="right" mt="xs">
            <Button
              variant="subtle"
              color="red"
              style={{ color: "#e94f4fd9" }}
              onClick={clearFormAll}
              disabled={reservationMutation.isLoading}>
              Sıfırla
            </Button>
            <Button
              onClick={() => reservationMutation.mutate()}
              loading={reservationMutation.isLoading}
              disabled={!selectedSlotId || reservationMutation.isSuccess}
              px="xl">
              Rezerve Et
            </Button>
          </Group>
        </Flex>
      </Paper>
    </Container>
  );
}

function ItemsTable({ items }: { items: ItemView[] | undefined }) {
  const { classes } = useStyles();

  if (!items) {
    return (
      <Text className={classes.boxedText} weight={400} size="sm">
        Masa envanterini listelemek için bir masa seçiniz
      </Text>
    );
  }

  if (items && items.length === 0) {
    return (
      <Text className={classes.boxedText} weight={400} size="sm">
        Masaya ait herhangi bir eşya bulunmamaktadır
      </Text>
    );
  }

  const rows = items.map((item) => (
    <tr key={item.id}>
      <td>{item.name}</td>
    </tr>
  ));

  return (
    <Table maw={620} withBorder verticalSpacing="xs" horizontalSpacing="md">
      <thead>
        <tr>
          <th>Eşya</th>
        </tr>
      </thead>
      <tbody>{rows}</tbody>
    </Table>
  );
}
