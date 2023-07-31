import { createStyles, Title, Text, Button, Container, Group } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

const useStyles = createStyles((theme) => ({
  root: {
    paddingTop: 80,
    paddingBottom: 80,
  },

  label: {
    textAlign: "center",
    fontWeight: 900,
    fontSize: 220,
    lineHeight: 1,
    marginBottom: parseInt(theme.spacing.xl) * 1.5,
    color: theme.colorScheme === "dark" ? theme.colors.dark[4] : theme.colors.gray[2],

    [theme.fn.smallerThan("sm")]: {
      fontSize: 120,
    },
  },

  title: {
    fontFamily: `Greycliff CF, ${theme.fontFamily}`,
    textAlign: "center",
    fontWeight: 900,
    fontSize: 38,

    [theme.fn.smallerThan("sm")]: {
      fontSize: 32,
    },
  },

  description: {
    maxWidth: 600,
    margin: "auto",
    marginTop: theme.spacing.xl,
    marginBottom: parseInt(theme.spacing.xl) * 1.5,
  },
}));

export function NotFound() {
  const { classes } = useStyles();
  const { t } = useTranslation();
  const navigate = useNavigate();

  function navigateToHomepage(event: React.MouseEvent<HTMLElement>) {
    navigate("/");
  }

  return (
    <Container className={classes.root}>
      <div className={classes.label}>404</div>
      <Title className={classes.title}>{t("404.title")}</Title>
      <Text color="dimmed" size="lg" align="center" className={classes.description}>
        {t("404.description")}
      </Text>
      <Group position="center">
        <Button variant="filled" size="md" onClick={navigateToHomepage}>
          {t("404.backToHomepage")}
        </Button>
      </Group>
    </Container>
  );
}
