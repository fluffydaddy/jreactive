/*
 * Copyright © 2024 fluffydaddy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fluffydaddy.jreactive;

import java.lang.ref.WeakReference;

public class WeakDataObserver<T> implements DataObserver<T>, DelegatingObserver<T> {
	private final WeakReference<DataObserver<T>> weakDelegate;
	private DataSubscription subscription;
	
	WeakDataObserver(DataObserver<T> delegate) {
		this.weakDelegate = new WeakReference<DataObserver<T>>(delegate);
	}
	
	@Override
	public void onData(T data) {
		DataObserver<T> delegate = weakDelegate.get();
		if (delegate != null) {
			delegate.onData(data);
		} else {
			subscription.cancel();
		}
	}
	
	@Override
	public DataObserver<T> getDelegate() {
		return weakDelegate.get();
	}
	
	public void setSubscription(DataSubscription subscription) {
		this.subscription = subscription;
	}
	
	@Override
    public boolean equals(Object other) {
        if (other instanceof WeakDataObserver) {
            DataObserver<T> delegate = weakDelegate.get();
            if (delegate != null && delegate == ((WeakDataObserver) other).weakDelegate.get()) {
                return true;
            }
            return super.equals(other);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        DataObserver<T> delegate = weakDelegate.get();
        if (delegate != null) {
            return delegate.hashCode();
        } else {
            return super.hashCode();
        }
    }
}
