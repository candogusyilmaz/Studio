import {
  createStyles,
  Container,
  Group,
  Burger,
  Text,
  ActionIcon,
  useMantineColorScheme,
  Menu,
  Avatar,
  UnstyledButton,
  Stack,
  Collapse,
} from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { IconCalendarEvent, IconHome, IconList, IconLogout, IconMoonStars, IconPlus, IconSettings, IconSun } from "@tabler/icons";
import { ReactElement, useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import { CustomNavLink } from "./CustomNavLink";
import { HeaderLink } from "./HeaderLink";

const useStyles = createStyles((theme) => ({
  header: {
    height: "56px",
    borderBottom: `1px solid ${theme.colorScheme === "dark" ? theme.colors.dark[5] : theme.colors.gray[2]}`,
  },

  headerContainer: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    height: "100%",
  },

  navbar: {
    height: "56px",
    borderBottom: `1px solid ${theme.colorScheme === "dark" ? theme.colors.dark[5] : theme.colors.gray[2]}`,
    [theme.fn.smallerThan("sm")]: {
      display: "none",
    },
  },

  navbarContainer: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    height: "100%",
  },

  navbarMobile: {
    [theme.fn.largerThan("sm")]: {
      display: "none",
    },
    borderBottom: `1px solid ${theme.colorScheme === "dark" ? theme.colors.dark[5] : theme.colors.gray[2]}`,
  },

  navbarContainerMobile: {
    display: "flex",
    flexDirection: "column",
    padding: "8px 0 8px 0",
    height: "100%",
  },

  user: {
    color: theme.colorScheme === "dark" ? theme.colors.dark[0] : theme.black,
    padding: `${theme.spacing.xs}px 0`,
    borderRadius: theme.radius.sm,
    transition: "background-color 100ms ease",
  },

  toggler: {
    [theme.fn.smallerThan("sm")]: {
      display: "none",
    },
    color: theme.colorScheme === "dark" ? theme.colors.gray[3] : theme.colors.dark[3],
    "&:hover": {
      backgroundColor: "transparent",
      color: theme.colorScheme === "dark" ? "#fff" : theme.colors.dark[8],
    },
  },

  profile: {
    [theme.fn.smallerThan("sm")]: {
      display: "none",
    },
  },

  burger: {
    [theme.fn.largerThan("sm")]: {
      display: "none",
    },
  },
}));

type RouteLink = {
  href?: string;
  label: string;
  permission?: string;
  icon?: ReactElement;
  links?: RouteLink[];
};

const routeLinks: RouteLink[] = [
  {
    href: "/",
    label: "Anasayfa",
    icon: <IconHome size={16} />,
  },
  {
    href: "/reservations",
    label: "Rezervasyon",
    icon: <IconCalendarEvent size={16} />,
    links: [
      {
        href: "/reservations/new",
        label: "Yeni",
        icon: <IconPlus size={16} />,
      },
      {
        href: "/reservations/history",
        label: "Geçmiş",
        icon: <IconList size={16} />,
      },
    ],
  },
];

function getInitials(text: string | undefined | null) {
  if (!text) {
    return "S";
  }

  const textSplit = text.toLocaleUpperCase().split(" ");

  if (textSplit.length === 1) {
    return textSplit[0][0];
  }

  return textSplit[0][0] + textSplit[textSplit.length - 1][0];
}

export function HeaderMenu() {
  const [opened, { toggle }] = useDisclosure(false);
  const { classes } = useStyles();
  const { colorScheme, toggleColorScheme } = useMantineColorScheme();
  const { getUser, logout } = useContext(AuthContext);

  const user = getUser();

  return (
    <>
      <header className={classes.header}>
        <Container className={classes.headerContainer} size="xl">
          <Burger opened={opened} onClick={toggle} className={classes.burger} size="sm" />
          <Text weight={600} size={28}>
            studio
          </Text>
          <Group spacing="xs">
            <ActionIcon className={classes.toggler} onClick={() => toggleColorScheme()} size="lg">
              {colorScheme === "dark" ? <IconSun size={18} /> : <IconMoonStars size={18} />}
            </ActionIcon>
            <Menu width={200} position="bottom-end" transition="fade">
              <Menu.Target>
                <UnstyledButton className={classes.user}>
                  <Group spacing={2}>
                    <Avatar src={null} alt={user?.displayName} size={32} radius="sm" mr={6}>
                      {getInitials(user?.displayName)}
                    </Avatar>
                    <Stack spacing={3} className={classes.profile}>
                      <Text weight={400} size={14} sx={{ lineHeight: 1 }}>
                        {user?.displayName}
                      </Text>
                      <Text c="dimmed" weight={300} size="xs" sx={{ lineHeight: 1 }}>
                        test
                      </Text>
                    </Stack>
                  </Group>
                </UnstyledButton>
              </Menu.Target>
              <Menu.Dropdown>
                <Menu.Label>Profil</Menu.Label>
                <Menu.Item icon={<IconSettings size={14} stroke={1.5} />}>Hesap Ayarları</Menu.Item>
                <Menu.Divider />
                <Menu.Item color="red" icon={<IconLogout size={14} stroke={1.5} />} onClick={logout}>
                  Çıkış
                </Menu.Item>
              </Menu.Dropdown>
            </Menu>
          </Group>
        </Container>
      </header>
      <nav className={classes.navbar}>
        <Container className={classes.navbarContainer} size="xl">
          <Group>
            {routeLinks.map((link) => (
              <HeaderLink {...link} key={link.href} />
            ))}
          </Group>
        </Container>
      </nav>
      <Collapse in={opened}>
        <nav className={classes.navbarMobile}>
          <div className={classes.navbarContainerMobile}>
            {routeLinks.map((link) => (
              <CustomNavLink {...link} key={link.href} />
            ))}
          </div>
        </nav>
      </Collapse>
    </>
  );
}
