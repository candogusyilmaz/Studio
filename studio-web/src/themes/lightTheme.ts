import { MantineThemeOverride } from "@mantine/core";

const lightTheme: MantineThemeOverride = {
  colorScheme: "light",
  primaryColor: "cyan",
  defaultRadius: "xs",
  fontFamily: "Public Sans",
  headings: { fontFamily: "Public Sans" },
  globalStyles: (theme) => ({
    body: {
      WebkitFontSmoothing: "antialiased",
    },
  }),
};

export default lightTheme;
