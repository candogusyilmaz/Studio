import { MantineThemeOverride } from "@mantine/core";

const lightTheme: MantineThemeOverride = {
  colorScheme: "light",
  colors: {
    primary: ["#afd5e9", "#9ccae3", "#88bfdd", "#74b5d8", "#368fbf", "#3386b3", "#2d779f", "#27688b", "#225977", "#163c50"],
    gradientFrom: ["#a3caf5", "#8cbdf2", "#75b0f0", "#4d99eb", "#1a7ce5", "#176fce", "#1563b7", "#1257a0", "#0f4a8a", "#0a315c"],
    gradientTo: ["#adebeb", "#98e6e6", "#84e1e1", "#69dbdb", "#31cece", "#2cb9b9", "#27a5a5", "#239090", "#1e7b7b", "#145252"],
  },
  primaryShade: 4,
  primaryColor: "primary",
  defaultRadius: "xs",
  other: {
    defaultBorderWidth: 1,
  },
  components: {
    Button: {
      styles: {
        root: {
          borderWidth: 1,
        },
      },
    },
    Chip: {
      styles: {
        label: {
          borderWidth: 1,
        },
      },
    },
    Input: {
      styles: {
        input: {
          borderWidth: 1,
        },
      },
    },
    Pagination: {
      styles: {
        item: {
          borderWidth: 1,
        },
      },
    },
    Switch: {
      styles: {
        track: {
          borderWidth: 1,
        },
      },
    },
  },
  fontFamily: "Inter",
  defaultGradient: {
    deg: 90,
    to: "gradientTo",
    from: "gradientFrom",
  },
  activeStyles: {
    transform: "scale(0.95)",
  },
};

export default lightTheme;
