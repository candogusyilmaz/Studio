import { IconArrowNarrowDown, IconArrowNarrowUp, IconSelector } from "@tabler/icons-react";

function SortArrow({ canSort, isSorted }: { canSort: boolean; isSorted: boolean | "asc" | "desc" }) {
  if (canSort && isSorted) {
    return (isSorted as string) === "asc" ? (
      <IconArrowNarrowUp style={{ paddingBottom: "0.05rem" }} size={14} stroke={3} />
    ) : (
      <IconArrowNarrowDown style={{ paddingBottom: "0.05rem" }} size={14} stroke={3} />
    );
  } else if (canSort && !isSorted) {
    return <IconSelector style={{ paddingBottom: "0.05rem" }} size={14} stroke={3} />;
  }

  return null;
}

export default SortArrow;
