import { Box, Container, createStyles, rem, Text } from "@mantine/core";
import { useLayoutEffect, useRef, useState } from "react";
import { Outlet } from "react-router-dom";
import { HeaderMenu } from "../components/header/HeaderMenu";

const useStyles = createStyles((theme) => ({
  footer: {
    borderTop: `${rem(1)} solid ${theme.colorScheme === "dark" ? theme.colors.dark[5] : theme.colors.gray[2]}`,
  },
}));

export default function StudioShell() {
  const { classes } = useStyles();
  const footerEl = useRef<HTMLDivElement>(null!);
  const [footerHeight, setFooterHeight] = useState(0);

  useLayoutEffect(() => {
    setFooterHeight(footerEl.current.offsetHeight ?? 0);
  }, [footerEl.current]);

  return (
    <>
      <Box
        component="main"
        sx={{
          minHeight: `calc(100vh - ${rem(footerHeight)})`,
        }}>
        <HeaderMenu />
        <Container size="xl" mt="md" mb="md">
          <Outlet />
        </Container>
      </Box>
      <footer className={classes.footer} ref={footerEl}>
        <Container size="xl" my="auto" py="xs">
          <Text c="dimmed" size="xs" fw="bold">
            v0.0.1-alpha
          </Text>
        </Container>
      </footer>
    </>
  );
}
