"use client"

import { useEffect, useState } from "react";
import { DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { DialogOrderFormData, useDialogOrderForm } from "./dialog-item-form"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { convertRealToAmerican, convertRealToCents } from "@/src/utils/convertCurrency";
import { toast } from "sonner";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Category, Item, Order } from "@/types/next-props";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Check, ChevronsUpDown } from "lucide-react";
import {
    Command,
    CommandGroup,
    CommandItem,
    CommandInput,
    CommandList,
    CommandEmpty,
  } from "@/components/ui/command"
import { cn } from "@/lib/utils";

export function DialogOrder({ closeModal, initialValues, onRefresh, orderId}: any){
    const form = useDialogOrderForm({ initialValues: initialValues });
    const [loading, setLoading] = useState(false);
    const [items, setItems] = useState<Item[]>([]);

    useEffect(() => {
        async function loadItems() {
            const res = await fetch("/api/items");
            const data = await res.json();
      
            setItems(data);
          }
    
        loadItems();
    }, [form]);

    async function onSubmit(values: DialogOrderFormData){
        const data = {
            "tableNumber": values.tableNumber,
            "itemsIds": values.itemsIds,
        }

        if(orderId){
            const result = await fetch(`/api/items/${orderId}`, {
                method: "PUT",
                headers: {
                  "Content-Type": "application/json",
                },
                body: JSON.stringify(data),
            });

            if(!result.ok){
                toast.error("Falha ao alterar item, tente novamente!");
                return;
            }

            setLoading(false);
            toast.success('Item alterado com sucesso!');
            onRefresh();
            handleCloseModal();
            return;
        }

        const result = await fetch("/api/demands", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        setLoading(false);

        if (!result.ok) {
            toast.error("Falha ao adicionar pedido, tente novamente!");
            return;
          }
        
        toast.success('Pedido adicionado com sucesso!');
        onRefresh();
        handleCloseModal();
    }

    function handleCloseModal(){
        form.reset();
        closeModal();
    }

    return (
        <>
            <DialogHeader>
                <DialogTitle>Novo Pedido</DialogTitle>
                <DialogDescription>
                    Adicionar novo Pedido
                </DialogDescription>
            </DialogHeader>

            <Form {...form}>
                <form className="space-y-2" onSubmit={form.handleSubmit(onSubmit)}>
                    <div className="flex flex-col">
                        <FormField control={form.control} name="tableNumber" render={ ({ field }) => (
                            <FormItem className="my-2">
                                <FormLabel className="font-semibold">
                                    Número da mesa:
                                </FormLabel>
                                <FormControl>
                                    <Input {...field} placeholder="Digite o número da mesa" />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}/>

                        <div className="my-4">
                        <FormField
                            control={form.control}
                            name="itemsIds"
                            render={({ field }) => (
                                <FormItem>
                                <FormLabel className="font-semibold">Selecione os itens:</FormLabel>

                                <FormControl>
                                    <Popover>
                                    <PopoverTrigger asChild>
                                        <Button variant="outline" className="w-full justify-between">
                                        {field.value?.length > 0
                                            ? `${field.value.length} itens selecionados`
                                            : "Selecione"}
                                        <ChevronsUpDown className="ml-2 h-4 w-4 opacity-50" />
                                        </Button>
                                    </PopoverTrigger>

                                    <PopoverContent className="w-[300px] p-0">
                                        <Command>
                                        <CommandInput placeholder="Procure pelo item..." />
                                        <CommandGroup>
                                            {items.map((item) => {
                                            const isSelected = field.value?.includes(item.id);

                                            return (
                                                <CommandItem
                                                key={item.id}
                                                onSelect={() => {
                                                    const updated = isSelected
                                                    ? field.value.filter((v: any) => v !== item.id)
                                                    : [...field.value, item.id];

                                                    field.onChange(updated);
                                                }}
                                                >
                                                <Check
                                                    className={cn(
                                                    "mr-2 h-4 w-4",
                                                    isSelected ? "opacity-100" : "opacity-0"
                                                    )}
                                                />
                                                    {item.name}
                                                </CommandItem>
                                            );
                                            })}
                                        </CommandGroup>
                                        </Command>
                                    </PopoverContent>
                                    </Popover>
                                </FormControl>

                                <FormMessage />
                                </FormItem>
                            )}
                            />

                            </div>
                    </div>

                    

                    <Button type="submit" className="w-full font-semibold text-white" disabled={loading}>
                        { loading ? "carregando..." : `${orderId ? "Alterar item" : "Adicionar novo pedido"}`}
                    </Button>
                </form>
            </Form>
        </>
    )
}