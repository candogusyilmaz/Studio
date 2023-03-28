import { ActionIcon, Blockquote, Center, createStyles, Text, TextInput } from "@mantine/core";
import { useForm, zodResolver } from "@mantine/form";
import { showNotification } from "@mantine/notifications";
import { IconBrandInertia, IconQuote } from "@tabler/icons-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { z } from "zod";
import { showErrorNotification } from "../../api/api";
import { createQuote, fetchQuoteOfTheDay } from "../../api/quoteService";

const useStyles = createStyles((theme) => ({
  blockquote: {
    paddingTop: "3rem",
    paddingBottom: "3rem",
    paddingLeft: "4rem",
    paddingRight: "4rem",

    [theme.fn.smallerThan("sm")]: {
      paddingTop: "0.5rem",
      paddingBottom: "0.5rem",
      paddingLeft: "2rem",
      paddingRight: "2rem",
    },
  },

  quote: {
    wordBreak: "break-word",
    fontSize: "4rem",
    [theme.fn.smallerThan("md")]: {
      fontSize: "2.5rem",
    },

    [theme.fn.smallerThan("sm")]: {
      fontSize: "1.5rem",
    },
  },

  input: {
    width: "600px",
    [theme.fn.smallerThan("md")]: {
      width: "300px",
    },

    [theme.fn.smallerThan("sm")]: {
      width: "200px",
    },
  },
}));

const queryKeys = {
  quetoOfTheDay: "quoteOfTheDay",
};

function useQuoteOfTheDay() {
  const query = useQuery({
    queryKey: [queryKeys.quetoOfTheDay],
    queryFn: ({ signal }) => fetchQuoteOfTheDay(signal),
    select: (data) => data.data,
    keepPreviousData: true,
    cacheTime: 24 * 60 * 1000,
    staleTime: 24 * 60 * 1000,
  });

  return { quote: query.data, isLoading: query.isLoading };
}

export default function Dashboard() {
  const { classes } = useStyles();

  const { quote: quoteOfTheDay, isLoading } = useQuoteOfTheDay();

  const schema = z.object({
    quote: z.string().min(1, { message: "Boş bir alıntı oluşturamazsın!" }),
  });

  const form = useForm({
    validate: zodResolver(schema),
    initialValues: {
      quote: "",
    },
  });

  const handleSubmit = form.onSubmit((_values) => {
    newQuoteMutation.mutate();
  });

  const newQuoteMutation = useMutation({
    mutationFn: () => createQuote(form.values.quote),
    onSuccess: () => {
      showNotification({
        id: "quote-create-success",
        title: "Alıntın Oluşturuldu",
        message: "Bir gün gösterilmek üzere alıntın oluşturuldu.",
        color: "green",
        autoClose: 5000,
      });
      form.reset();
    },
    onError: (error: AxiosError, _variables, _context) => {
      showErrorNotification(error, { id: "quote-create-error" });
    },
  });

  return (
    <>
      <Center>
        {!isLoading && (
          <Blockquote color="blue" cite={`- ${quoteOfTheDay?.user?.displayName ?? "Sistem"}`} className={classes.blockquote}>
            <Text className={classes.quote}>{quoteOfTheDay?.content ?? "Buralar biraz ıssız gibi bir şeyler eklemeye ne dersin?"}</Text>
          </Blockquote>
        )}
      </Center>
      <Center>
        <form onSubmit={handleSubmit}>
          <TextInput
            pb="1rem"
            className={classes.input}
            description="sözünü herkese duyurmak istemez misin?"
            icon={<IconQuote size={16} />}
            rightSection={
              <ActionIcon title="Alıntımı bir gün gösterilmek üzere oluştur" type="submit">
                <IconBrandInertia size={18} />
              </ActionIcon>
            }
            {...form.getInputProps("quote")}
          />
        </form>
      </Center>
    </>
  );
}
