"use client";

import { useState } from "react"
import { usePathname } from "next/navigation";
import clsx from "clsx";
import {
    Sheet,
    SheetContent,
    SheetDescription,
    SheetTitle,
    SheetTrigger,
  } from "@/components/ui/sheet";

import {
    Collapsible,
    CollapsibleContent,
} from "@/components/ui/collapsible"
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { Banknote, CalendarCheck2, ChevronLeft, ChevronRight, Folder, FolderTree, List, LogOut, Package, Settings, ShoppingCart } from "lucide-react";
import { logout } from "@/lib/auth";

export function SidebarDashboard({ children }: { children: React.ReactNode }){
    
    const pathname = usePathname();
    const [isCollapsed, setIsCollapsed] = useState(false);
    const [isOpen, setIsOpen] = useState(false);

    return (
        <>
            <div className="flex min-h-screen w-full">

                <aside className={clsx("flex flex-col justify-between border-r bg-background transition-all duration-300 p-4 h-full", {
                    "w-20": isCollapsed,
                    "w-64": !isCollapsed,
                    "hidden md:flex md:fixed": true
                })}>
                    <div className="w-full h-full mr-auto flex flex-col items-end">
                        <Button className="bg-gray-100 hover:bg-gray-50 text-zinc-900 self-end mb-2" onClick={() => setIsCollapsed(!isCollapsed)}>
                            {isCollapsed ? <ChevronRight className="w-12 h-12"></ChevronRight> : <ChevronLeft className="w-12 h-12"></ChevronLeft>}
                        </Button>

                        {
                            isCollapsed && (
                                <nav className="flex flex-col justify-between h-full gap-1 overflow-hidden mt-2">
                                    <div>
                                        <SideBarLink 
                                            href="/itens"
                                            label="Itens"
                                            pathname={pathname}
                                            isCollapsed={isCollapsed}
                                            setIsCollapsed={setIsCollapsed}
                                            setIsOpen={setIsOpen}
                                            icon={<Package className="w-6 h-6"/>}
                                        />

                                        <SideBarLink 
                                            href="/categorias"
                                            label="Categorias"
                                            pathname={pathname}
                                            isCollapsed={isCollapsed}
                                            setIsCollapsed={setIsCollapsed}
                                            setIsOpen={setIsOpen}
                                            icon={<FolderTree className="w-6 h-6"/>}
                                        />

                                        <SideBarLink 
                                            href="/pedidos"
                                            label="Pedidos"
                                            pathname={pathname}
                                            isCollapsed={isCollapsed}
                                            setIsCollapsed={setIsCollapsed}
                                            setIsOpen={setIsOpen}
                                            icon={<ShoppingCart className="w-6 h-6"/>}
                                        />

                                        <SideBarLink 
                                            href="/perfil"
                                            label="Perfil"
                                            pathname={pathname}
                                            isCollapsed={isCollapsed}
                                            setIsCollapsed={setIsCollapsed}
                                            setIsOpen={setIsOpen}
                                            icon={<Settings className="w-6 h-6"/>}
                                        /> 
                                    </div>
                                    
                                    <SideBarLink 
                                        href="#"
                                        label="Sair"
                                        pathname={pathname}
                                        isCollapsed={isCollapsed}
                                        setIsCollapsed={setIsCollapsed}
                                        setIsOpen={setIsOpen}
                                        icon={<LogOut className="w-6 h-6"/>}
                                        event={logout}
                                    />
                                </nav>
                            )
                        }

                        <Collapsible open={!isCollapsed} className="w-full">
                            <CollapsibleContent>
                            <nav className="flex flex-col gap-1 overflow-hidden">
                                <span className="text-sm text-gray-400 font-medium mt-1 uppercase">
                                    Painel
                                </span>
                                <SideBarLink 
                                        href="/itens"
                                        label="Itens"
                                        pathname={pathname}
                                        isCollapsed={isCollapsed}
                                        setIsCollapsed={setIsCollapsed}
                                        setIsOpen={setIsOpen}
                                        icon={<Package className="w-6 h-6"/>}
                                    />

                                    <SideBarLink 
                                        href="/categorias"
                                        label="Categorias"
                                        pathname={pathname}
                                        isCollapsed={isCollapsed}
                                        setIsCollapsed={setIsCollapsed}
                                        setIsOpen={setIsOpen}
                                        icon={<FolderTree className="w-6 h-6"/>}
                                    />

                                    <SideBarLink 
                                        href="/pedidos"
                                        label="Pedidos"
                                        pathname={pathname}
                                        isCollapsed={isCollapsed}
                                        setIsCollapsed={setIsCollapsed}
                                        setIsOpen={setIsOpen}
                                        icon={<ShoppingCart className="w-6 h-6"/>}
                                    />
                                <span className="text-sm text-gray-400 font-medium mt-1 uppercase">
                                    Configuracoes
                                </span>

                                <SideBarLink 
                                    href="/perfil"
                                    label="Perfil"
                                    pathname={pathname}
                                    isCollapsed={isCollapsed}
                                    setIsCollapsed={setIsCollapsed}
                                    setIsOpen={setIsOpen}
                                    icon={<Settings className="w-6 h-6"/>}
                                />
                            </nav>
                            </CollapsibleContent>
                        </Collapsible>
                    </div>

                    {
                        !isCollapsed && (
                            <SideBarLink 
                                href="#"
                                label="Sair"
                                pathname={pathname}
                                isCollapsed={isCollapsed}
                                setIsCollapsed={setIsCollapsed}
                                setIsOpen={setIsOpen}
                                icon={<LogOut className="w-6 h-6"/>}
                                event={logout}
                            />                            
                    )}

                </aside>

                <div className={clsx("flex flex-1 flex-col transition-all duration-300", {
                    "md:ml-20" : isCollapsed,
                    "md:ml-64" : !isCollapsed
                })}>
                    <header className="md:hidden flex items-center justify-between border-b p-4 md:px-6 h-14 z-10 sticky top-0 bg-white">
                        <Sheet open={isOpen} onOpenChange={setIsOpen}>
                            <div className="flex items-center gap-4">
                                <SheetTrigger asChild>
                                    <Button variant="outline" size="icon" className="md:hidden" onClick={() => setIsCollapsed(false)}>
                                        <List className="w-5 h-5" />
                                    </Button>
                                </SheetTrigger>

                                <h1 className="text-base md:text-lg font-semibold">Menu OdontoPro</h1>
                            </div>

                            <SheetContent side="right" className="sm:max-w-xs text-black p-4">
                                <SheetTitle>OdontoPro</SheetTitle>
                                <SheetDescription>
                                    Menu Administrativo
                                </SheetDescription>

                                <nav className="grid gap-2 text-base pt-5">
                                    <SideBarLink 
                                        href="/dashboard"
                                        label="Appointments"
                                        pathname={pathname}
                                        isCollapsed={isCollapsed}
                                        setIsCollapsed={setIsCollapsed}
                                        setIsOpen={setIsOpen}
                                        icon={< CalendarCheck2 className="w-6 h-6"/>}
                                    />

                                    <SideBarLink 
                                        href="/dashboard/services"
                                        label="Services"
                                        pathname={pathname}
                                        isCollapsed={isCollapsed}
                                        setIsCollapsed={setIsCollapsed}
                                        setIsOpen={setIsOpen}
                                        icon={<Folder className="w-6 h-6"/>}
                                    />

                                    <SideBarLink 
                                        href="/dashboard/profile"
                                        label="My Profile"
                                        pathname={pathname}
                                        isCollapsed={isCollapsed}
                                        setIsCollapsed={setIsCollapsed}
                                        setIsOpen={setIsOpen}
                                        icon={<Settings className="w-6 h-6"/>}
                                    />

                                    <SideBarLink 
                                        href="/dashboard/plans"
                                        label="Plans"
                                        pathname={pathname}
                                        isCollapsed={isCollapsed}
                                        setIsCollapsed={setIsCollapsed}
                                        setIsOpen={setIsOpen}
                                        icon={<Banknote className="w-6 h-6"/>}
                                    />
                                </nav>
                            </SheetContent>
                        </Sheet>
                    </header>

                    {/* Main Content */}
                    <main className="flex-1 py-4 px-2 md:p-6">
                        {children}            
                    </main>
                </div>
            </div>   
        </>
    )
}

interface SideBarLinkProps {
    href: string;
    icon: React.ReactNode;
    label: string;
    pathname: string;
    isCollapsed: boolean;
    setIsCollapsed: React.Dispatch<React.SetStateAction<boolean>>;
    setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
    event?: () => void;
}

function SideBarLink({ href, icon, label, pathname, isCollapsed, setIsCollapsed, setIsOpen, event } : SideBarLinkProps){
    const handleClick = (e: React.MouseEvent) => {
        if (event) {
          e.preventDefault(); 
          event();
        }
      };

    return (
        <Link href={href} onClick={handleClick}>
            <div className={clsx("flex items-center gap-2  px-3 py-2 rounded-md transition-colors", {
                "text-white bg-blue-500": pathname === href,
                "text-grey-700 hover:bg-gray-100": pathname !== href,
            })}>
                <span className="w-6 h-6">{icon}</span>
                {!isCollapsed && <span>{label}</span>}
            </div>
        </Link>
    )
}
