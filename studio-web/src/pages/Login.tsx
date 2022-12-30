import { Paper, createStyles, TextInput, PasswordInput, Button, Title, Alert } from "@mantine/core";
import { useForm, zodResolver } from "@mantine/form";
import { IconAlertCircle } from "@tabler/icons";
import { useContext } from "react";
import { useTranslation } from "react-i18next";
import { z } from "zod";
import { AuthContext } from "../context/AuthContext";

const useStyles = createStyles((theme) => ({
  wrapper: {
    minHeight: "100vh",
    backgroundSize: "cover",
    backgroundImage:
      "url(https://images.unsplash.com/photo-1507090960745-b32f65d3113a?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80)",
  },

  form: {
    borderRight: `1px solid ${theme.colorScheme === "dark" ? theme.colors.dark[7] : theme.colors.gray[3]}`,
    minHeight: "100vh",
    maxWidth: 650,
    padding: 140,
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "strech",

    [`@media (max-width: ${theme.breakpoints.md}px)`]: {
      padding: 30,
      maxWidth: "100%",
    },
  },

  title: {
    color: theme.colorScheme === "dark" ? theme.white : theme.black,
  },

  logo: {
    color: theme.colorScheme === "dark" ? theme.white : theme.black,
    width: 120,
    display: "block",
    marginLeft: "auto",
    marginRight: "auto",
  },
}));

export function Login() {
  const { isClientError, isLoggingIn, login } = useContext(AuthContext);
  const { classes } = useStyles();
  const { t } = useTranslation();

  const schema = z.object({
    username: z.string().min(1, { message: t("login.errors.usernameEmpty")! }),
    password: z.string().min(1, { message: t("login.errors.passwordEmpty")! }),
  });

  const form = useForm({
    validate: zodResolver(schema),
    initialValues: {
      username: "",
      password: "",
    },
  });

  const handleLogin = form.onSubmit((values) => {
    if (form.isValid() === false) return;

    login(values.username, values.password);
  });

  return (
    <div className={classes.wrapper}>
      <Paper className={classes.form} radius={0}>
        <Title order={2} className={classes.title} align="center" mt="md" mb={50}>
          {t("login.title")}
        </Title>
        {isClientError && (
          <Alert icon={<IconAlertCircle size={24} />} color="red" mb={20} variant="outline">
            {t("login.errors.wrongCredentials")}
          </Alert>
        )}
        <form onSubmit={handleLogin}>
          <TextInput
            label={t("login.username")}
            placeholder={t("login.usernamePlaceHolder")}
            size="sm"
            {...form.getInputProps("username")}
          />
          <PasswordInput
            label={t("login.password")}
            placeholder={t("login.passwordPlaceholder")}
            mt="md"
            size="sm"
            {...form.getInputProps("password")}
          />
          {/* <Checkbox label="Keep me logged in" mt="xl" size="sm" /> */}
          <Button fullWidth mt="xl" size="sm" type="submit" loading={isLoggingIn}>
            {t("login.login")}
          </Button>
        </form>
        {/* <Text align="center" mt="md">
          Don&apos;t have an account?{" "}
          <Anchor<"a">
            href="#"
            weight={700}
            onClick={(event) => event.preventDefault()}
          >
            Register
          </Anchor>
        </Text> */}
      </Paper>
    </div>
  );
}
