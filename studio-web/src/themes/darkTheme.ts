import { MantineThemeOverride } from "@mantine/core";

const darkTheme: MantineThemeOverride = {
  colorScheme: "dark",
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

export default darkTheme;
