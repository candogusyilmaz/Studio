import { ReactElement, useContext } from "react";
import { Group, createStyles, Anchor, Menu, SimpleGrid } from "@mantine/core";
import { IconChevronDown } from "@tabler/icons";
import { useLocation, useNavigate } from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";

const useStyles = createStyles((theme) => ({
  mainLink: {
    height: "56px",
    lineHeight: "56px",
    fontSize: 14,
    color: theme.colorScheme === "dark" ? theme.colors.dark[1] : theme.colors.dark[4],
    padding: "0 8px",
    fontWeight: 500,
    borderBottom: "1px solid transparent",
    transition: "border-color 100ms ease, color 100ms ease",

    "&:hover": {
      color: theme.colorScheme === "dark" ? theme.white : theme.black,
      textDecoration: "none",
    },
  },

  mainLinkActive: {
    color: theme.colorScheme === "dark" ? theme.colors.dark[1] : theme.colors.dark[6],
    borderBottomColor: theme.colors[theme.primaryColor][theme.colorScheme === "dark" ? 5 : 6],
  },

  subLinkActive: {
    backgroundColor: "1px solid red"
  }
}));

export interface HeaderLinkProps {
  href?: string;
  label: string;
  permission?: string;
  icon?: ReactElement;
  links?: HeaderLinkProps[];
}

export function HeaderLink({ href, label, permission, icon, links }: HeaderLinkProps) {
  const navigate = useNavigate();
  const location = useLocation();
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
      <Menu.Item
        key={subLink.href}
        onClick={(event) => {
          event.preventDefault();
          if (subLink.href) navigate(subLink.href);
        }}
        className={cx({ [classes.subLinkActive]: subLink.href === location.pathname })}
      >
        <Group spacing={6}>
          {subLink.icon}
          {subLink.label}
        </Group>
      </Menu.Item>
    ));

  return (
    <>
      <Menu offset={-8} position="bottom-start" transitionProps={{ transition: "fade" }} withArrow arrowOffset={40}>
        <Menu.Target>
          <Anchor<"a">
            key={href}
            href={href}
            className={cx(classes.mainLink, {
              [classes.mainLinkActive]: href === location.pathname || links?.some((s) => s.href === location.pathname),
            })}
            onClick={(event) => {
              event.preventDefault();
              if (href && !links) navigate(href);
            }}>
            <Group spacing={6}>
              {icon}
              {label}
              {subItems.length > 0 && <IconChevronDown size={14} />}
            </Group>
          </Anchor>
        </Menu.Target>
        {subItems.length > 0 && (
          <Menu.Dropdown>
            <SimpleGrid cols={subItems.length < 4 ? 1 : 2}>{subItems}</SimpleGrid>
          </Menu.Dropdown>
        )}
      </Menu>
    </>
  );
}
