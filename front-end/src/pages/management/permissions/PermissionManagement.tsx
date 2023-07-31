import { useQuery } from "@tanstack/react-query";
import { SortingState, createColumnHelper } from "@tanstack/react-table";
import { useState } from "react";
import { fetchPermissions } from "../../../api/permissionService";
import { PermissionView } from "../../../api/types";
import StudioTable from "../../../components/shared/StudioTable/StudioTable";
import Page1 from "../../../layouts/Page1";

const columnHelper = createColumnHelper<PermissionView>();
const columns = [
  columnHelper.accessor((row) => row.id, {
    id: "id",
    header: "#",
  }),
  columnHelper.accessor((row) => row.displayName, {
    id: "displayName",
    header: "Tanım",
  }),
  columnHelper.accessor((row) => row.name, {
    id: "name",
    header: "Yetki Adı",
  }),
];

export default function PermissionManagement() {
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<SortingState>([{ id: "name", desc: false }]);

  const permissionsQuery = useQuery({
    queryKey: ["permissions", { page, sort }],
    queryFn: ({ signal }) => fetchPermissions(page, sort, signal),
    select: (data) => data.data,
  });

  return (
    <Page1 header="Yetki Tanımlamaları">
      <StudioTable
        status={permissionsQuery.status}
        data={permissionsQuery.data?.content ?? []}
        columns={columns}
        sort={sort}
        setSort={setSort}
        pagination={{ page, setPage, total: permissionsQuery.data?.totalPages ?? 0 }}
      />
    </Page1>
  );
}
