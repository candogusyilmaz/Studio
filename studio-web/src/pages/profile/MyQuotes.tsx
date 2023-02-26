import { Badge, Button, Flex, Menu, Pagination, Switch, Text, Tooltip } from "@mantine/core";
import { IconDotsVertical } from "@tabler/icons";
import { useMutation, useQuery } from "@tanstack/react-query";
import { createColumnHelper, SortingState } from "@tanstack/react-table";
import { AxiosError } from "axios";
import { useState, useMemo } from "react";
import { showErrorNotification } from "../../api/api";
import { fetchMyQuotes, toggleQuoteStatus } from "../../api/quoteService";
import { getQuoteStatus, getQuoteStatusColor, QuoteView } from "../../api/types";
import BasicTable from "../../components/BasicTable";
import { convertDateToLocaleDateString } from "../../utils/DateTimeUtils";

const queryKeys = {
  myQuotes: "myQuotes",
};

function useQuotes(page: number, sort: SortingState) {
  const quotes = useQuery({
    queryKey: [queryKeys.myQuotes, { page, sort }],
    queryFn: ({ signal }) => fetchMyQuotes(page, sort, signal),
    staleTime: Infinity,
    select: (data) => data.data,
    keepPreviousData: true,
  });

  return quotes;
}

export default function MyQuotes() {
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<SortingState>([{ id: "id", desc: true }]);
  const quotes = useQuotes(page, sort);

  const toggleQuoteStatusMutation = useMutation({
    mutationFn: (id: number) => toggleQuoteStatus(id),
    onSuccess: () => {
      quotes.refetch();
    },
    onError(error: any) {
      showErrorNotification(error);
    },
  });

  const columnHelper = createColumnHelper<QuoteView>();
  const columns = useMemo(
    () => [
      columnHelper.accessor((row) => row.id, {
        id: "id",
        header: "#",
        size: 20,
      }),
      columnHelper.accessor((row) => row.content, {
        id: "content",
        header: "İçerik",
        enableSorting: false,
        cell: (row) => (
          <Tooltip label={row.getValue()} position="bottom">
            <Text truncate="end" maw={300}>
              {row.getValue()}
            </Text>
          </Tooltip>
        ),
      }),
      columnHelper.accessor((row) => row.status, {
        id: "status",
        header: "Durum",
        cell: (row) => <Badge color={getQuoteStatusColor(row.getValue())}>{getQuoteStatus(row.getValue())}</Badge>,
      }),
      columnHelper.accessor((row) => row.shownTimes, {
        id: "shownTimes",
        header: "Gösterilme Sayısı",
      }),
      columnHelper.accessor((row) => row.lastShownDate, {
        id: "lastShownDate",
        header: "Son Gösterilme Tarihi",
        cell: (row) => row.getValue() && convertDateToLocaleDateString(row.getValue()!),
      }),
      columnHelper.accessor((row) => row.statusResetDate, {
        id: "statusResetDate",
        header: "Durum Sıfırlanma Tarihi",
        cell: (row) => row.getValue() && convertDateToLocaleDateString(row.getValue()!),
      }),
      columnHelper.accessor((row) => row, {
        id: "enabled",
        header: "Etkin",
        cell: (row) => (
          <Switch
            //disabled={row.getValue().status === "ACTIVE"}
            checked={row.getValue().enabled}
            onChange={() => toggleQuoteStatusMutation.mutate(row.getValue().id)}
          />
        ),
      }),
    ],
    [],
  );

  return (
    <Flex my="xl" direction="column" gap="xs">
      <Text size="xl" weight={600} mb="sm">
        Alıntılarım
      </Text>
      <BasicTable
        data={quotes.data?.content ?? []}
        columns={columns}
        sort={sort}
        setSort={setSort}
        pagination={{ page, setPage, total: quotes.data?.totalPages ?? 1 }}
      />
    </Flex>
  );
}
