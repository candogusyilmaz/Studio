import { useContext } from "react";
import { Group, createStyles, Anchor, Text, Collapse } from "@mantine/core";
import { IconChevronDown } from "@tabler/icons";
import { useLocation, useNavigate } from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";
import { HeaderLinkProps } from "./HeaderLink";
import { useDisclosure } from "@mantine/hooks";

const useStyles = createStyles((theme) => ({
  mainLink: {
    fontSize: 14,
    color: theme.colorScheme === "dark" ? theme.colors.gray[5] : theme.colors.dark[4],
    padding: "8px 0px",
    fontWeight: 400,
    transition: "border-color 100ms ease, color 100ms ease",

    "&:hover": {
      color: theme.colorScheme === "dark" ? theme.white : theme.black,
      textDecoration: "none",
    },
  },

  mainLinkActive: {
    color: theme.colorScheme === "dark" ? theme.colors.gray[2] : theme.black,
  },

  subLink: {
    color: theme.colorScheme === "dark" ? theme.colors.gray[6] : theme.colors.dark[3],

    "&:hover": {
      color: theme.colorScheme === "dark" ? theme.white : theme.black,
      textDecoration: "none",
      cursor: "pointer",
    },
  },

  subLinkActive: {
    color: theme.colorScheme === "dark" ? theme.colors.gray[3] : theme.colors.dark[8],
    backgroundColor: theme.colorScheme === "dark" ? theme.colors.dark[6] : theme.colors.gray[1],
  },
}));

export function MobileHeaderLink({ href, label, permission, icon, links }: HeaderLinkProps) {
  const navigate = useNavigate();
  const location = useLocation();
  const [opened, { toggle }] = useDisclosure(false);
  const { cx, classes } = useStyles();
  const { getUser } = useContext(AuthContext);

  const user = getUser();

  if (permission && !user?.permissions.includes(permission)) {
    return <></>;
  }

  const hasSubItems = links && links.length > 0;
  const subItems = (hasSubItems ? links : [])
    .filter((s) => (s.permission ? user?.permissions.includes(s.permission) : true))
    .map((subLink) => (
      <Group
        pl={36}
        py={4}
        spacing={6}
        key={subLink.href}
        onClick={(event) => {
          event.preventDefault();
          if (subLink.href) {
            navigate(subLink.href);
            toggle();
          }
        }}
        className={cx(classes.subLink, { [classes.subLinkActive]: subLink.href === location.pathname })}>
        <Text size={12} weight={400}>
          {subLink.label}
        </Text>
      </Group>
    ));

  return (
    <>
      <Anchor<"a">
        key={href}
        href={href}
        className={cx(classes.mainLink, {
          [classes.mainLinkActive]: href === location.pathname || links?.some((s) => s.href === location.pathname),
        })}
        onClick={(event) => {
          event.preventDefault();
          if (href && !links) navigate(href);
          toggle();
        }}>
        <Group position="apart" px={12}>
          <Group spacing={8}>
            {icon}
            {label}
          </Group>
          {subItems.length > 0 && <IconChevronDown size={14} />}
        </Group>
      </Anchor>
      {subItems.length > 0 && (
        <Collapse in={opened} my={-4}>
          {subItems}
        </Collapse>
      )}
    </>
  );
}
