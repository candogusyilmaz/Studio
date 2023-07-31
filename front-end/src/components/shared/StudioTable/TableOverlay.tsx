import { Box, Center, LoadingOverlay, Overlay, Text } from "@mantine/core";

export default function TableOverlay({ status }: { status: "loading" | "error" }) {
  const h = "min(60vh, 600px)";

  switch (status) {
    case "loading":
      return (
        <Box pos="relative" h={h}>
          <LoadingOverlay loaderProps={{ variant: "bars" }} transitionDuration={6000} visible />
        </Box>
      );
    case "error":
      return (
        <Box pos="relative" h={h}>
          <Overlay
            styles={(theme) => ({
              root: {
                backgroundColor: theme.colorScheme === "dark" ? theme.colors.dark[6] : theme.colors.gray[0],
                border: "1px solid rgba(255,0,0,0.4)",
              },
            })}>
            <Center h={h}>
              <Text>Bir hata oluştu!</Text>
            </Center>
          </Overlay>
        </Box>
      );
    default:
      return null;
  }
}
