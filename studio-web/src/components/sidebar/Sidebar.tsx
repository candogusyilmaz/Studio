import {
  Navbar,
  Group,
  ScrollArea,
  createStyles,
  MediaQuery,
  Burger,
  useMantineTheme,
} from "@mantine/core";
import {
  IconCalendarStats,
  IconGauge,
  IconPresentationAnalytics,
  IconFileAnalytics,
  IconAdjustments,
  IconLock,
  Icon123,
} from "@tabler/icons";
import { ColorSchemeToggler } from "./ColorSchemeToggler";
import { LinksGroup } from "./LinksGroup";
import { UserButton } from "./UserButton";

const mockdata = [
  { label: "Dashboard", icon: IconGauge },
  {
    label: "Market news",
    icon: Icon123,
    initiallyOpened: true,
    links: [
      { label: "Overview", link: "/" },
      { label: "Forecasts", link: "/" },
      { label: "Outlook", link: "/" },
      { label: "Real time", link: "/" },
    ],
  },
  {
    label: "Releases",
    icon: IconCalendarStats,
    links: [
      { label: "Upcoming releases", link: "/" },
      { label: "Previous releases", link: "/" },
      { label: "Releases schedule", link: "/asdgasd" },
    ],
  },
  { label: "Analytics", icon: IconPresentationAnalytics },
  { label: "Contracts", icon: IconFileAnalytics },
  { label: "Settings", icon: IconAdjustments },
  {
    label: "Security",
    icon: IconLock,
    links: [
      { label: "Enable 2FA", link: "/" },
      { label: "Change password", link: "/" },
      { label: "Recovery codes", link: "/" },
    ],
  },
];

const useStyles = createStyles((theme) => ({
  navbar: {
    backgroundColor:
      theme.colorScheme === "dark" ? theme.colors.dark[6] : theme.white,
  },

  header: {
    padding: theme.spacing.md,
    color: theme.colorScheme === "dark" ? theme.white : theme.black,
    borderBottom: `1px solid ${
      theme.colorScheme === "dark" ? theme.colors.dark[4] : theme.colors.gray[3]
    }`,
  },

  links: {
  },

  linksInner: {
    paddingTop: theme.spacing.xl,
    paddingBottom: theme.spacing.xl,
  },

  footer: {
    borderTop: `1px solid ${
      theme.colorScheme === "dark" ? theme.colors.dark[4] : theme.colors.gray[3]
    }`,
  },
}));

export function Sidebar({ opened, setOpened }: SidebarProps) {
  const theme = useMantineTheme();
  const { classes } = useStyles();
  const links = mockdata.map((item) => (
    <LinksGroup {...item} key={item.label} />
  ));

  return (
    <Navbar
      width={{ sm: 275 }}
      className={classes.navbar}
      hiddenBreakpoint="sm"
      hidden={!opened}
    >
      <Navbar.Section className={classes.header}>
        <Group position="apart">
          Studio
          <MediaQuery largerThan="sm" styles={{ display: "none" }}>
            <Burger
              opened={opened}
              onClick={() => setOpened()}
              size="sm"
              color={theme.colors.gray[6]}
              mr="xl"
            />
          </MediaQuery>
          <ColorSchemeToggler />
        </Group>
      </Navbar.Section>

      <Navbar.Section grow className={classes.links} component={ScrollArea}>
        <div className={classes.linksInner}>{links}</div>
      </Navbar.Section>

      <Navbar.Section className={classes.footer}>
        <UserButton name="Can" email="can@email.com" image="" />
      </Navbar.Section>
    </Navbar>
  );
}

interface SidebarProps {
  opened: boolean;
  setOpened: Function;
};
