import { PaginationProps } from "@mantine/core";
import { SortingState } from "@tanstack/react-table";
import { Dispatch, SetStateAction } from "react";

export interface StudioTableProps<T> {
  data: T[];
  columns: any[];
  sort?: SortingState;
  setSort?: Dispatch<SetStateAction<SortingState>>;
  pagination?: {
    page: number;
    setPage: Dispatch<SetStateAction<number>>;
    total: number;
    props?: Omit<PaginationProps, "total" | "value" | "onChange">;
  };
  header?: {
    leftSection?: React.ReactNode;
    rightSection?: React.ReactNode;
    options?: {
      showDivider?: boolean;
      showColumnVisibilityButton?: boolean;
    };
  };
  status?: "loading" | "error" | "success";
}
