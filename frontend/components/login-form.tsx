"use client"

import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"

import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"

import {
  Field,
  FieldDescription,
  FieldGroup,
} from "@/components/ui/field"
import { Input } from "@/components/ui/input"
import backgroundImage from "@/public/menu-manager-background.jpg";
import Image from "next/image"
import { useForm } from "react-hook-form";
import { useState } from "react"
import { Eye, EyeOff } from "lucide-react"
import { apiPost } from "@/lib/api"
import { useRouter } from "next/navigation";
import { toast } from 'sonner';
import { setToken } from "@/lib/auth"

export function LoginForm({
  className,
  ...props
}: React.ComponentProps<"div">) {
  const router = useRouter();

  const form = useForm({
    defaultValues: {
      username: "",
      password: "",
    },
  });

  async function onSubmit(data: any) {

    const response = await fetch("/api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });

    const result = await response.json();

    if(result.error){
      toast.error("Credenciais inválidas, tente novamente!");
      return;
    }

    await setToken(result.token);

    router.push("/");
  }

  const [showPassword, setShowPassword] = useState(false);

  function togglePassword() {
    setShowPassword((prev) => !prev);
  }

  return (
    <div className={cn("flex flex-col gap-6", className)} {...props}>
      <Card className="overflow-hidden p-0">
        <CardContent className="grid p-0 md:grid-cols-2">
        <Form {...form}>
          <form className="p-6 md:p-8" onSubmit={form.handleSubmit(onSubmit)}>
            <FieldGroup>
              <div className="flex flex-col items-center gap-2 text-center">
                <h1 className="text-2xl font-bold">Seja bem vindo</h1>
                <p className="text-muted-foreground text-balance">
                  Login Menu Manager
                </p>
              </div>
              <Field>
                  <FormField control={form.control} name="username" render={({ field }) => (
                    <FormItem>
                        <FormLabel>E-mail:</FormLabel>
                        <FormControl>
                          <Input {...field} type="email" placeholder="Digite seu e-mail" />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                     )}
                    />
              </Field>
              <Field>
                  <FormField control={form.control} name="password" render={({ field }) => (
                    <FormItem>
                        <FormLabel>Senha:</FormLabel>
                        <FormControl>
                        <div className="relative">
                          <Input
                            {...field}
                            type={showPassword ? "text" : "password"}
                            placeholder="Digite sua senha"
                            className="pr-10"
                          />
                          <Button
                            type="button"
                            variant="ghost"
                            size="icon"
                            onClick={togglePassword}
                            className="absolute right-1 top-1/2 -translate-y-1/2 hover:bg-transparent"
                          >
                            {showPassword ? (
                              <EyeOff className="h-4 w-4 text-gray-500" />
                            ) : (
                              <Eye className="h-4 w-4 text-gray-500" />
                            )}
                          </Button>
                        </div>
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                     )}
                    />
              </Field>
              <Field>
                <Button type="submit">Login</Button>
              </Field>
            </FieldGroup>
          </form>
          </Form>
          <div className="bg-muted relative hidden md:block">
            <Image src={backgroundImage} alt="Background Image" className="h-full object-cover" quality={100} priority/>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
