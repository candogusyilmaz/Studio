import { useState } from "react";

/**
 * Gets the value from the local storage by the key.
 * If there is no value by the key, it returns the default value if given.
 * @param key The key to store the value in localStorage
 * @param defaultValue The default value to use if there is no value by the key in localStorage
 * @returns The value from localStorage or the default value
 */
function usePersistedState<T>(key: string, defaultValue?: T) {
  let localStorageValue;

  try {
    const item = localStorage.getItem(key);
    localStorageValue = item ? JSON.parse(item) : undefined;
  } catch (error) {
    localStorageValue = undefined;
  }

  const [value, setValue] = useState<T | undefined>(localStorageValue || defaultValue);

  if (!localStorageValue && defaultValue) {
    persist(defaultValue);
  }

  function persist(value: T) {
    if (value) localStorage.setItem(key, JSON.stringify(value));
    else localStorage.removeItem(key);

    setValue(value);
  }

  return [value, persist] as const;
}

export default usePersistedState;
