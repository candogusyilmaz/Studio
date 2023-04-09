import { Flex } from "@mantine/core";
import React from "react";
import PageHeader from "../components/PageHeader";

export default function Page1({ header, children }: { header: string; children?: React.ReactNode }) {
  return (
    <Flex my="xl" direction="column" gap="xs">
      <PageHeader>{header}</PageHeader>
      {children}
    </Flex>
  );
}
