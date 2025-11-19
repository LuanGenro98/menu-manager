"use client"

import { useDialogOrderForm } from "./dialog-item-form"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { toast } from "sonner";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Order, OrderStatusProp } from "@/types/next-props";
import { status } from "@/src/utils/statusOrders";

interface DialogOrderProps {
    onRefresh: () => void;
    orderId?: number;
    initialValues: {
        status: string;
    };
}

export function OrderStatus({ initialValues, onRefresh, orderId}: DialogOrderProps){
    const form = useDialogOrderForm({ initialValues: initialValues });

    async function changeStatus(value: OrderStatusProp){
        
        const data = {
            "newStatus": value.status
        };
        
        const result = await fetch(`/api/demands/${orderId}/status`, {
            method: "PATCH",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        if(!result.ok){
            toast.error("Falha ao alterar status, tente novamente!");
            return;
        }

        toast.success('Status alterado com sucesso!');
        onRefresh();
        return;
    }

    return (
        <>
            <Form {...form}>
                <form className="space-y-2">
                    <div className="flex flex-col">
                    <FormField
                        control={form.control}
                        name="status"
                        render={({ field }) => (
                            <FormItem className="my-2">
                            <FormLabel className="font-semibold">Alterar status:</FormLabel>

                            <FormControl>
                            <Select
                                value={String(field.value) || ""}
                                onValueChange={(value) => {
                                    field.onChange(value);
                                    changeStatus({ status: value});
                                }}
                            >
                                <SelectTrigger>
                                    <SelectValue placeholder="Selecione o status" />
                                </SelectTrigger>

                                <SelectContent>
                                    {status.map((item) => (
                                    <SelectItem key={item.id} value={item.value}>
                                        {item.label}
                                    </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>

                            </FormControl>

                            <FormMessage />
                            </FormItem>
                        )}
                        />
                    </div>
                </form>
            </Form>
        </>
    )
}