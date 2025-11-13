import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";

export const LoginSchema = z.object({
  email: z.string().email("Invalid email").min(1, "Email is required"),
  password: z.string().min(6, "Password must be at least 6 characters"),
});

export type LoginFormData = z.infer<typeof LoginSchema>;

export interface UseLoginFormProps {
  email?: string | null;
  password?: string | null;
}

export function useLoginForm({ email, password }: UseLoginFormProps) {
  return useForm<LoginFormData>({
    resolver: zodResolver(LoginSchema),
    defaultValues: {
      email: email ?? "",
      password: password ?? "",
    },
  });
}
