'use client'

import { Button } from "@/components/ui/button"
import { LoginFormData, useLoginForm } from "./use-login-form"; // Fixed import path
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"

import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
  } from "@/components/ui/form"

import { Input } from "@/components/ui/input"
import { toast } from 'sonner';

export default function LoginForm({ login }: any) {

    const form = useLoginForm({
        email: login?.email,
        password: login?.password,
    });

    async function onSubmit(values: LoginFormData) {
        try {            
            const response = await fetch("/api/login", { 
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(values),
            });

            if (!response.ok) {
                toast.error(response.statusText);
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();

            toast.success(data);
            
        } catch (error) {
            console.error("Login failed:", error);
        }
    }

    return (
        <div className="flex justify-center items-center h-screen">
            <Card className="w-full flex align-items-center max-w-sm h-min">
                <CardHeader>
                    <CardTitle className="text-center">Login to your account</CardTitle>
                </CardHeader>
                <CardContent>
                    <Form {...form}>
                        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
                            <FormField 
                                control={form.control} 
                                name="email" 
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>E-mail:</FormLabel>
                                        <FormControl>
                                            <Input {...field} placeholder="E-mail:" type="email" />
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />

                            <FormField 
                                control={form.control} 
                                name="password" 
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>Password:</FormLabel>
                                        <FormControl>
                                            <Input {...field} placeholder="Password" type="password" />
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            
                            {/* Submit button moved inside form */}
                            <div className="flex flex-col gap-2">
                                <Button type="submit" className="w-full cursor-pointer">
                                    Login
                                </Button>
                                <Button type="button" variant="outline" className="w-full cursor-pointer">
                                    Register
                                </Button>
                            </div>
                        </form>
                    </Form>
                </CardContent>
            </Card>
        </div>
    )
}