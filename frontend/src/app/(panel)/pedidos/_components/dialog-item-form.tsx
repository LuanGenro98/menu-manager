import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import z from 'zod'

const formSchema = z.object({
    tableNumber: z.coerce.number().min(1),
    itemsIds: z.array(z.number()).min(1),
    status: z.string()
})  

export interface UseDialogOrderFormProps {
    initialValues?: {
        tableNumber?: string;
        itemsIds?: number[];
        status: string;
    }
}

export type DialogOrderFormData = z.infer<typeof formSchema>;

export function useDialogOrderForm({ initialValues }: UseDialogOrderFormProps){
    return useForm<any>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            tableNumber: initialValues?.tableNumber
                ? Number(initialValues.tableNumber)
                : 0,
            itemsIds: initialValues?.itemsIds ?? [],
            status: initialValues?.status ?? "ORDERED",
        }
    })
}
