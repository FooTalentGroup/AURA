import { useState, useEffect } from 'react';
import { Patient } from '../../../types/patient.ts';

const mockPatients: Patient[] = [
  {
    id: '1',
    name: 'Juan Pérez',
    age: 32,
    dni: '30.123.456',
    nextSession: '10 Mayo a las 14:00',
    lastSession: '03 Mayo a las 14:00',
    contact: 'juanperez@mail.com',
    avatarUrl: '/images/avatar-placeholder.png',
  },
  {
    id: '2',
    name: 'María Gómez',
    age: 29,
    dni: '33.987.654',
    nextSession: '11 Mayo a las 16:00',
    lastSession: '04 Mayo a las 16:00',
    contact: 'maria@mail.com',
    avatarUrl: '/images/avatar-placeholder.png',
  },
  // Agregá más pacientes si lo necesitás
];
export function usePatients() {
    const [patients, setPatients] = useState<Patient[]>([]);
    const [loading, setLoading] = useState(true);
  
    useEffect(() => {
      const timer = setTimeout(() => {
        setPatients(mockPatients);
        setLoading(false);
      }, 300);
      return () => clearTimeout(timer);
    }, []);
  
    return { patients, loading };
  }